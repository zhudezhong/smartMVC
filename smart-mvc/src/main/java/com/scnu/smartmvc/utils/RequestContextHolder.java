package com.scnu.smartmvc.utils;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.servlet.http.HttpServletRequest;

/**
 * 在当前线程中存放了当前请求的`HttpServletRequest`
 */
public class RequestContextHolder {
    private static final ThreadLocal<HttpServletRequest> inheritableRequestHolder = new NamedInheritableThreadLocal<>("Request context");

    public static void resetRequest() {
        inheritableRequestHolder.remove();
    }

    public static void setRequest(HttpServletRequest request) {
        inheritableRequestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return inheritableRequestHolder.get();
    }
}
