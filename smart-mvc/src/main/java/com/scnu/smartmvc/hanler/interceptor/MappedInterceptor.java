package com.scnu.smartmvc.hanler.interceptor;

import com.scnu.smartmvc.ModelAndView;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * `MappedInterceptor`所需要完成的功能：
 * 1. 作为真正`HandlerInterceptor`的代理类，所以需要继承于`HandlerInterceptor`，实现`HandlerInterceptor`的三个接口，
 * 并且内部需要包含真正`HandlerInterceptor`的实例
 * 2. 管理`interceptor`对哪些URL生效，排除哪些URL
 * 3. 提供match功能，调用方传入path，判断当前`HandlerInterceptor`是否支持本次请求。该功能简单实现，只支持path的完整匹配，
 * 需要了解更复杂的匹配请查看SpringMVC中的`MappedInterceptor`
 */
public class MappedInterceptor implements HandlerInterceptor {

    private List<String> includePatterns = new ArrayList<>();
    private List<String> excludePatterns = new ArrayList<>();

    private HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor handlerInterceptor) {
        this.interceptor = handlerInterceptor;
    }

    /**
     * 添加支持的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addIncludePatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 添加排除的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addExcludePatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 根据传入的path，判断当前的interceptor是否支持
     *
     * @param lookupPath
     * @return
     */
    public boolean matches(String lookupPath) {
        // 在排除的路径不能匹配
        if (!CollectionUtils.isEmpty(excludePatterns) && excludePatterns.contains(lookupPath)) {
            return false;
        }

        if (ObjectUtils.isEmpty(this.includePatterns)) {
            return true;
        }

        if (includePatterns.contains(lookupPath)) {
            return true;
        }

        return false;
    }

    /**
     * 在执行Handler之前被调用，如果返回的是false，那么Handler就不会在执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.interceptor.preHandle(request, response, handler);
    }


    /**
     * 在Handler执行完成之后被调用，可以获取Handler返回的结果`ModelAndView`
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 该方法是无论什么情况下都会被调用，比如：`preHandle`返回false，Handler执行过程中抛出异常，Handler正常执行完成
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }

    public List<String> getIncludePatterns() {
        return includePatterns;
    }

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }
}
