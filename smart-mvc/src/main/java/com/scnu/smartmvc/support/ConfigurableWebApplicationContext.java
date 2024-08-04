package com.scnu.smartmvc.support;

import org.springframework.context.ConfigurableApplicationContext;

import javax.servlet.ServletContext;

public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {

    void setServletContext(ServletContext servletContext);
}
