package com.scnu.smartmvc.hanler;

import com.scnu.smartmvc.hanler.interceptor.HandlerInterceptor;
import com.scnu.smartmvc.ModelAndView;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerExecutionChain {
    // 根据request中的path找到匹配的`HandlerMethod`，也就是控制器中的某个方法
    private HandlerMethod handler;

    // 根据request中的path找到所有对本次请求生效的拦截器`HandlerInterceptor`
    private List<HandlerInterceptor> interceptors;

    // 记录当前执行拦截器的下标
    private int interceptorIndex = -1;

    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if (!CollectionUtils.isEmpty(interceptors)) {
            this.interceptors = interceptors;
        }
    }

    /**
     * 执行所有拦截器的preHandle方法，如果preHandle返回的是false，那么就执行triggerAfterCompletion
     *
     * @param request
     * @param response
     */
    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CollectionUtils.isEmpty(this.interceptors)) {
            return true;
        }

        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);

            // 如果preHandle返回的是false，那么就执行triggerAfterCompletion
            if (!interceptor.preHandle(request, response, this.handler)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }

            this.interceptorIndex = i;
        }

        return true;
    }

    /**
     * 执行所有拦截器的postHandle方法
     *
     * @param request
     * @param response
     * @param mv
     */
    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (CollectionUtils.isEmpty(this.interceptors)) {
            return;
        }

        for (int i = interceptors.size() - 1; i > 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(request, response, this.handler, mv);
        }

    }

    /**
     * `HandlerExecutionChain`中还定义了一个变量`interceptorIndex`，
     * 当每执行一个`HandlerInterceptor`的preHandle方法后`interceptorIndex`的值就会被修改成当前执行拦截器的下标，
     * `triggerAfterCompletion`中根据`interceptorIndex`记录的下标值反向执行拦截器的`afterCompletion`方法；
     *
     * @param request
     * @param response
     * @param ex
     */
    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        if (CollectionUtils.isEmpty(this.interceptors)) {
            return;
        }

        for (int i = this.interceptorIndex; i > 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request, response, this.handler, ex);
        }
    }


    public HandlerMethod getHandler() {
        return handler;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }
}
