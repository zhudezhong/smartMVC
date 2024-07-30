package com.scnu.smartmvc;

import com.scnu.smartmvc.hanler.HandlerExecutionChain;
import com.scnu.smartmvc.hanler.adapter.HandlerAdapter;
import com.scnu.smartmvc.hanler.interceptor.HandlerInterceptor;
import com.scnu.smartmvc.hanler.mapping.HandlerMapping;
import com.scnu.smartmvc.utils.RequestContextHolder;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.view.resolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;


    /**
     * 初始化方法.
     * 对诸如 handlerMapping，ViewResolver 等进行初始化
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
    }

    /**
     * 对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其service方法;
     * 通过`RequestContextHolder`在线程变量中设置request，然后调用`doDispatch`完成请求
     *
     * @param request  the {@link HttpServletRequest} object that
     *                 contains the request the client made of
     *                 the servlet
     * @param response the {@link HttpServletResponse} object that
     *                 contains the response the servlet returns
     *                 to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("DispatcherServlet.service => uri: {}", request.getRequestURI());
        RequestContextHolder.setRequest(request);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            logger.error("Handler the request fail", e);
        } finally {
            RequestContextHolder.resetRequest();
        }

    }

    /**
     * 在`doDispatch`方法中的执行逻辑
     * <p>
     * - 首先通过handlerMapping获取到处理本次请求的`HandlerExecutionChain`
     * - 执行拦截器的前置方法
     * - 通过`handlerAdapter`执行handler返回ModelAndView
     * - 执行拦截器的后置方法
     * - 处理返回的结果`processDispatchResult`
     * - 在处理完成请求后调用`executionChain.triggerAfterCompletion(request, response, dispatchException);`，完成拦截器的`afterCompletion`方法调用
     *
     * @param request
     * @param response
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Exception dispectchException = null;
        HandlerExecutionChain executionChain = null;

        try {
            ModelAndView mv = null;
            try {
                executionChain = this.handlerMapping.getHandler(request);
                if (!executionChain.applyPreHandle(request, response)) {
                    return;
                }
                // Actually invoke the handler
                mv = handlerAdapter.handle(request, response, executionChain.getHandler());

                executionChain.applyPostHandle(request, response, mv);
            } catch (Exception e) {
                dispectchException = e;
            }
            processDispatchResult(request, response, mv, dispectchException);
        } catch (Exception ex) {

            dispectchException = ex;
            throw ex;

        } finally {

            if (Objects.nonNull(executionChain)) {
                executionChain.triggerAfterCompletion(request, response, dispectchException);
            }
        }
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, Exception ex) throws Exception {

        if (Objects.nonNull(ex)) {
            // error ModelAndView
            mv = processHandlerException(request, response, ex);
        }

        if (Objects.nonNull(mv)) {
            render(mv, request, response);
        }

    }

    /**
     * 首先通过ViewResolver解析出视图，然后在调用View的render方法实施渲染逻辑
     *
     * @param mv
     * @param request
     * @param response
     */
    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view;
        String viewName = mv.getViewName();
        if (!StringUtils.isEmpty(viewName)) {
            view = this.viewResolver.resolveViewName(viewName);
        } else {
            view = (View) mv.getView();
        }

        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().getValue());
        }

        view.render(mv.getModel().asMap(), request, response);
    }

    /**
     * 返回的是一个异常处理后返回的ModeAndView
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
