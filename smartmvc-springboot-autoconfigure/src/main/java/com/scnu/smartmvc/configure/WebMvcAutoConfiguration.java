package com.scnu.smartmvc.configure;

import com.scnu.smartmvc.annotation.EnableWebMvc;
import com.scnu.smartmvc.config.WebMvcConfigurationSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@Import({SmartMvcDispatcherServletAutoConfiguration.class, ServletWebServerFactoryAutoConfiguration.class})
public class WebMvcAutoConfiguration {
    @EnableWebMvc
    @EnableConfigurationProperties({WebMvcProperties.class})
    public static class EnableWebMvcAutoConfiguration {
    }
}
