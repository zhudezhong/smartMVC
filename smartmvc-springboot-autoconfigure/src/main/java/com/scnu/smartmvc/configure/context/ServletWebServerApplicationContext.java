package com.scnu.smartmvc.configure.context;

import com.scnu.smartmvc.support.GenericWebApplicationContext;
import com.scnu.smartmvc.support.WebApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;

public class ServletWebServerApplicationContext extends GenericWebApplicationContext implements WebServerApplicationContext {
    // 定义webServer，这是SpringBoot中的类。有多个实现：Tomcat、jetty等
    private WebServer webServer;

    public ServletWebServerApplicationContext() {
    }


    @Override
    public WebServer getWebServer() {
        return this.webServer;
    }


    //try-catch整个容器的refresh过程，一旦出现任何异常，都需要关闭掉WebServer
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException ex) {
            WebServer webServer = this.webServer;
            if (webServer != null) {
                webServer.stop();
            }
            throw ex;
        }

    }

    //onRefresh是IOC容器提供的方法，允许用户在容器启动过程中做一些事情，这里我们就来创建Web服务器以及启动Web服务器
    @Override
    protected void onRefresh() throws BeansException {
        super.onRefresh();
        try {
            this.webServer = createWebServer();
            this.webServer.start();
        } catch (Throwable ex) {
            throw new ApplicationContextException("Unable to start web server", ex);
        }
    }

    // 调用ServletWebServerFactory创建Web服务器
    private WebServer createWebServer() {
        ServletWebServerFactory factory = getBeanFactory().getBean(ServletWebServerFactory.class);
        return factory.getWebServer(this::selfIInitialize);
    }

    //ServletContextInitializer 在Web容器启动完成后会回调此方法，比如：向ServletConext中添加DispatchServlet
    private void selfIInitialize(ServletContext servletContext) throws ServletException {
        prepareWebApplicationContext(servletContext);
        Map<String, ServletContextInitializer> beanMaps = getBeanFactory().getBeansOfType(ServletContextInitializer.class);
        for (ServletContextInitializer bean : beanMaps.values()) {
            bean.onStartup(servletContext);
        }
    }

    //在servletContext中保存ApplicationContext，容器中保存servletContext的引用
    private void prepareWebApplicationContext(ServletContext servletContext) {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this);
        setServletContext(servletContext);
    }

}
