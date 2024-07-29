package com.scnu.smartmvc.hanler.mapping;

import com.scnu.smartmvc.annotation.RequestMapping;
import com.scnu.smartmvc.http.RequestMethod;

/**
 * 主要是对应配置在控制器方法上的`RequestMapping`注解，
 * 把`RequestMapping`注解转换成`RequestMappingInfo`对象
 */
public class RequestMappingInfo {
    private String path;
    private RequestMethod httpMethod;

    public RequestMappingInfo(String prefix, RequestMapping httpMethod) {
        this.path = prefix + httpMethod.path();
        this.httpMethod = httpMethod.method();
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getHttpMethod() {
        return httpMethod;
    }
}
