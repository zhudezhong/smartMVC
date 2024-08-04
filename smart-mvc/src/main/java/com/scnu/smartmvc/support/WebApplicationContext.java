package com.scnu.smartmvc.support;

import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * `WebApplicationContext`是在IOC容器`ApplicationContext`的基础上来扩展，提供了获取`ServletContext`的方法；
 */
public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();
}
