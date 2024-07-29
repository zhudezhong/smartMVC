package com.scnu.smartmvc.hanler.interceptor;

import com.scnu.smartmvc.ModelAndView;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {
    /**
     * 在执行Handler之前被调用，如果返回的是false，那么Handler就不会在执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    ;

    /**
     * 在Handler执行完成之后被调用，可以获取Handler返回的结果`ModelAndView`
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView) throws Exception;

    /**
     * 该方法是无论什么情况下都会被调用，比如：`preHandle`返回false，Handler执行过程中抛出异常，Handler正常执行完成
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                         @Nullable Exception ex) throws Exception;
}
