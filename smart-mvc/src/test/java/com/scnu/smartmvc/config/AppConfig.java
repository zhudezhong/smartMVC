package com.scnu.smartmvc.config;

import com.scnu.smartmvc.DispatcherServlet;
import com.scnu.smartmvc.handler.interceptor.Test2HandlerInterceptor;
import com.scnu.smartmvc.handler.interceptor.TestHandlerInterceptor;
import com.scnu.smartmvc.hanler.adapter.HandlerAdapter;
import com.scnu.smartmvc.hanler.adapter.RequestMappingHandlerAdapter;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.interceptor.MappedInterceptor;
import com.scnu.smartmvc.hanler.mapping.HandlerMapping;
import com.scnu.smartmvc.hanler.mapping.RequestMappingHandlerMapping;
import com.scnu.smartmvc.view.resolver.ContentNegotiatingViewResolver;
import com.scnu.smartmvc.view.resolver.InternalResourceViewResolver;
import com.scnu.smartmvc.view.resolver.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.scnu.smartmvc")
public class AppConfig {

    /**
     * @Bean public RequestMappingHandlerMapping handlerMapping() {
     * InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
     * <p>
     * TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
     * <p>
     * interceptorRegistry.addInterceptor(interceptor)
     * .addIncludePatterns("/in_test")
     * .addExcludePatterns("/ex_test");
     * <p>
     * Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
     * interceptorRegistry.addInterceptor(interceptor2)
     * .addIncludePatterns("/in_test2", "/in_test3");
     * <p>
     * RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
     * requestMappingHandlerMapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
     * return requestMappingHandlerMapping;
     * }
     **/

    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setConversionService(conversionService);

        return adapter;
    }

    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        return negotiatingViewResolver;
    }

    @Bean
    ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yy-MM-dd HH:mm:ss");

        conversionService.addFormatter(dateFormatter);

        return conversionService;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }


}
