package com.scnu.smartmvc.configure;

import com.scnu.smartmvc.DispatcherServlet;
import com.scnu.smartmvc.configure.servlet.SmartMvcDispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties(WebMvcProperties.class) // 开启配置类WebMvcProperties
public class SmartMvcDispatcherServletAutoConfiguration {
    public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "smartMvcDispatcherServlet";

    @Bean
    @ConditionalOnMissingBean(value = DispatcherServlet.class)
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    @ConditionalOnBean(value = DispatcherServlet.class)
    public SmartMvcDispatcherServletRegistrationBean dispatcherServletRegistrationBean(
            DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {

        // 通过webMvcProperties配置Servlet的拦截路径
        SmartMvcDispatcherServletRegistrationBean registration =
                new SmartMvcDispatcherServletRegistrationBean(dispatcherServlet, webMvcProperties.getServlet().getPath());

        registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
        registration.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());

        return registration;
    }
}
