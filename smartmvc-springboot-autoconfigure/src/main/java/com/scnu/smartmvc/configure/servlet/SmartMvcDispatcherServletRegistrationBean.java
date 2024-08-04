package com.scnu.smartmvc.configure.servlet;

import com.scnu.smartmvc.DispatcherServlet;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.util.Assert;

public class SmartMvcDispatcherServletRegistrationBean extends ServletRegistrationBean<DispatcherServlet>
        implements DispatcherServletPath {

    private final String path; // 指定Servlet拦截的路径

    public SmartMvcDispatcherServletRegistrationBean(DispatcherServlet servlet, String path) {
        super(servlet);
        Assert.notNull(path, "path must not be null");
        this.path = path;
        super.addUrlMappings(getServletUrlMapping());
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
