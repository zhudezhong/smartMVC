package com.scnu.smartmvc.config;

import com.scnu.smartmvc.hanler.argument.HandlerMethodArgumentResolver;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.returnvalue.HandlerMethodReturnValueHandler;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 为了能够允许用户配置多个`WebMvcConfigurer`，
 * 所有和之前的参数解析器一样我们实现了一个聚合实现`WebMvcConfigurerComposite`
 */
public class WebMvcConfigurerComposite implements WebMvcConfigurer {
    private List<WebMvcConfigurer> delegates = new ArrayList<>();

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        delegates.forEach(configurer -> configurer.addArgumentResolvers(argumentResolvers));
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        delegates.forEach(configurer -> configurer.addReturnValueHandlers(returnValueHandlers));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        delegates.forEach(configurer -> configurer.addInterceptors(registry));
    }

    @Override
    public void addFormatters(FormatterRegistry registrar) {
        delegates.forEach(configurer -> configurer.addFormatters(registrar));
    }

    @Override
    public void addDefaultViews(List<View> views) {
        delegates.forEach(configurer -> configurer.addDefaultViews(views));
    }

    @Override
    public void addViewResolvers(List<ViewResolver> resolvers) {
        delegates.forEach(configurer -> configurer.addViewResolvers(resolvers));
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(WebMvcConfigurer... webMvcConfigurers) {
        Collections.addAll(this.delegates, webMvcConfigurers);
        return this;
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
        this.delegates.addAll(configurers);
        return this;
    }

}
