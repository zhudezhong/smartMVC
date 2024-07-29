package com.scnu.smartmvc.config;

import com.scnu.smartmvc.handler.interceptor.Test2HandlerInterceptor;
import com.scnu.smartmvc.handler.interceptor.TestHandlerInterceptor;
import com.scnu.smartmvc.hanler.interceptor.InterceptorRegistry;
import com.scnu.smartmvc.hanler.interceptor.MappedInterceptor;
import com.scnu.smartmvc.hanler.mapping.RequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.scnu.smartmvc")
public class AppConfig {

    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addInterceptor(interceptor)
                .addIncludePatterns("/in_test")
                .addExcludePatterns("/ex_test");

        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor2)
                .addIncludePatterns("/in_test2", "/in_test3");

        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
        return requestMappingHandlerMapping;
    }


}
