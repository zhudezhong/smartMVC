package com.scnu.smartmvc.config;

import com.scnu.smartmvc.hanler.argument.HandlerMethodArgumentResolver;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.returnvalue.HandlerMethodReturnValueHandler;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.List;

public interface WebMvcConfigurer {
    /**
     * 参数解析器的扩展点
     *
     * @param argumentResolvers
     */
    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

    /**
     * 返回值处理器扩展点
     *
     * @param returnValueHandlers
     */
    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }

    /**
     * 拦截器扩展点
     *
     * @param registry
     */
    default void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * 数据转换格式化的扩展点
     *
     * @param registrar
     */
    default void addFormatters(FormatterRegistry registrar) {
    }

    /**
     * 视图的扩展点
     *
     * @param views
     */
    default void addDefaultViews(List<View> views) {
    }

    /**
     * 视图解析器的扩展点
     *
     * @param resolvers
     */
    default void addViewResolvers(List<ViewResolver> resolvers) {
    }
}
