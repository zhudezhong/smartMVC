package com.scnu.smartmvc.config;

import com.scnu.smartmvc.hanler.adapter.HandlerAdapter;
import com.scnu.smartmvc.hanler.adapter.RequestMappingHandlerAdapter;
import com.scnu.smartmvc.hanler.argument.HandlerMethodArgumentResolver;
import com.scnu.smartmvc.hanler.exception.ExceptionHandlerExceptionResolver;
import com.scnu.smartmvc.hanler.exception.HandlerExceptionResolver;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.interceptor.MappedInterceptor;
import com.scnu.smartmvc.hanler.mapping.HandlerMapping;
import com.scnu.smartmvc.hanler.mapping.RequestMappingHandlerMapping;
import com.scnu.smartmvc.hanler.returnvalue.HandlerMethodReturnValueHandler;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.view.resolver.ContentNegotiatingViewResolver;
import com.scnu.smartmvc.view.resolver.InternalResourceViewResolver;
import com.scnu.smartmvc.view.resolver.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 初始化出DispatchServlet所有需要使用到的组件，并且预留一些可供用户扩展的接口。
 */
public class WebMvcConfigurationSupport {

    private List<MappedInterceptor> interceptors;
    private List<HandlerMethodReturnValueHandler> returnValueHandlers;
    private List<HandlerMethodArgumentResolver> argumentResolvers;

    /**
     * 构建数据转换器`FormattingConversionService`,预留给用户可以自定义转换格式的接口供子类覆写
     *
     * @return
     */
    @Bean
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;
    }

    /**
     * 数据转化格式化暴露对外的扩展点
     *
     * @param registry
     */
    protected void addFormatters(FormatterRegistry registry) {
    }

    /**
     * 提供给用户添加自定义拦截器的扩展点，默认系统不添加任何拦截器
     *
     * @param mvcConversionService
     * @return
     */
    protected List<MappedInterceptor> getInterceptors(FormattingConversionService mvcConversionService) {
        if (this.interceptors == null) {
            InterceptorRegistry registry = new InterceptorRegistry();
            addInterceptors(registry);
            this.interceptors = registry.getMappedInterceptors();
        }

        return this.interceptors;
    }

    /**
     * 拦截器暴露对外的扩展点
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * 构建HandlerMapping
     *
     * @param mvcConversionService
     * @return
     */
    @Bean
    public HandlerMapping handlerMapping(FormattingConversionService mvcConversionService) {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setInterceptors(getInterceptors(mvcConversionService));
        return handlerMapping;
    }

    /**
     * 构建HandlerAdapter，
     * 预留用户自定义参数解析器和返回值处理器，如果用户设置了就添加到`RequestMappingHandlerAdapter`
     *
     * @param conversionService
     * @return
     */
    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.setCustomArgumentResolvers(getArgumentResolvers());
        handlerAdapter.setCustomReturnValueHandlers(getReturnValueHandlers());
        return handlerAdapter;
    }

    protected List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        if (this.returnValueHandlers == null) {
            this.returnValueHandlers = new ArrayList<>();
            addReturnValueHandlers(this.returnValueHandlers);
        }
        return this.returnValueHandlers;
    }

    /**
     * 返回值解析器扩展点
     *
     * @param returnValueHandlers
     */
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }

    protected List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if (this.argumentResolvers == null) {
            this.argumentResolvers = new ArrayList<>();
            addArgumentResolvers(this.argumentResolvers);
        }
        return this.argumentResolvers;
    }

    private void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }


    /**
     * 构建全局异常处理器，同样需要设置参数解析器和返回值处理器
     *
     * @param conversionService
     * @return
     */
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(ConversionService mvcConversionService) {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setCustomArgumentResolvers(getArgumentResolvers());
        exceptionResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
        exceptionResolver.setConversionService(mvcConversionService);

        return exceptionResolver;
    }

    /**
     * 构建内容协同器`ContentNegotiatingViewResolver`，默认添加的视图解析器是`InternalResourceViewResolver`
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();

        List<ViewResolver> viewResolvers = new ArrayList<>();
        addViewResolvers(viewResolvers);
        if (CollectionUtils.isEmpty(viewResolvers)) {
            negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        } else {
            negotiatingViewResolver.setViewResolvers(viewResolvers);
        }

        List<View> views = new ArrayList<>();
        addDefaultViews(views);
        if (!CollectionUtils.isEmpty(views)) {
            negotiatingViewResolver.setDefaultViews(views);
        }

        return negotiatingViewResolver;
    }

    // 视图的扩展点
    protected void addDefaultViews(List<View> views) {
    }

    // 视图解析器的扩展点
    protected void addViewResolvers(List<ViewResolver> viewResolvers) {
    }


}
