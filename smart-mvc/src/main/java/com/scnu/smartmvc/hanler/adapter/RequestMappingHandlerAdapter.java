package com.scnu.smartmvc.hanler.adapter;

import com.scnu.smartmvc.ModelAndView;
import com.scnu.smartmvc.hanler.HandlerMethod;
import com.scnu.smartmvc.hanler.InvocableHandlerMethod;
import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import com.scnu.smartmvc.hanler.argument.*;
import com.scnu.smartmvc.hanler.returnvalue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    /**
     * 考虑到框架的扩展性，所以这里定义了`customArgumentResolvers`、`customReturnValueHandlers`两个变量，
     * 如果SmartMVC提供的参数解析器和返回值处理器不满足用户的需求，允许添加自定义的参数解析器和返回值处理器
     */
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;


    /**
     * 先通过传入`HandlerMethod`创建之前我们已经开发完成的组件`InvocableHandlerMethod`，
     * 然后调用`invokeAndHandle`执行控制器的方法
     * 当执行完成控制器的方法，我们需要通过`ModelAndViewContainer`创建`ModelAndView`对象返回
     *
     * @param request
     * @param response
     * @param handlerMethod
     * @return
     * @throws Exception
     */
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        InvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        invocableMethod.invokeAndHandle(request, response, mavContainer);

        return getModelAndview(mavContainer);

    }

    private ModelAndView getModelAndview(ModelAndViewContainer mavContainer) {
        if (mavContainer.isRequestHandled()) {
            // 本次请求已经处理完成
            return null;
        }

        ModelAndView mav = new ModelAndView();

        mav.setModel(mavContainer.getModel());
        mav.setStatus(mavContainer.getHttpStatus());
        mav.setView(mavContainer.getView());

        return mav;
    }

    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod,
                argumentResolverComposite,
                returnValueHandlerComposite,
                conversionService);
    }

    /**
     * 把系统默认支持的参数解析器和返回值处理器以及用户自定义的一起添加到系统中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "ConversionService can not null");

        // 初始化参数解析器
        if (Objects.isNull(argumentResolverComposite)) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolver(resolvers);
        }

        // 初始化返回值处理器
        if (Objects.isNull(returnValueHandlerComposite)) {
            List<HandlerMethodReturnValueHandler> resolvers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandler(resolvers);
        }


    }

    /**
     * 初始化默认返回值处理器
     *
     * @return
     */
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        // 2、添加返回值处理器
        handlers.add(new MapMethodReturnValueHandler());
        handlers.add(new ModelMethodReturnValueHandler());
        handlers.add(new ResponseBodyMethodReturnValueHandler());
        handlers.add(new ViewNameMethodReturnValueHandler());
        handlers.add(new ViewMethodReturnValueHandler());

        if (!CollectionUtils.isEmpty(getCustomReturnValueHandlers())) {
            handlers.addAll(getCustomReturnValueHandlers());
        }

        return handlers;
    }

    /**
     * 初始化默认的参数解析器
     *
     * @return
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // 2、添加返回值处理器
        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(getCustomArgumentResolvers())) {
            resolvers.addAll(getCustomArgumentResolvers());
        }

        return resolvers;
    }

    private void addCustomArgumentResolvers(HandlerMethodArgumentResolver... resolvers) {
        Collections.addAll(this.customArgumentResolvers, resolvers);
    }

    private void addCustomReturnValueHandlers(HandlerMethodReturnValueHandler... resolvers) {
        Collections.addAll(this.customReturnValueHandlers, resolvers);
    }

    // getter and setter methods
    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public HandlerMethodArgumentResolverComposite getArgumentResolverComposite() {
        return argumentResolverComposite;
    }

    public void setArgumentResolverComposite(HandlerMethodArgumentResolverComposite argumentResolverComposite) {
        this.argumentResolverComposite = argumentResolverComposite;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        return returnValueHandlerComposite;
    }

    public void setReturnValueHandlerComposite(HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite) {
        this.returnValueHandlerComposite = returnValueHandlerComposite;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
