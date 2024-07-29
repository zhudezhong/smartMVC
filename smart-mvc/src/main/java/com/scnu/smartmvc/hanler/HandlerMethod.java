package com.scnu.smartmvc.hanler;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * `HandlerMethod`是个很重要的对象，主要是对应控制器中的方法（Controller中的每个方法），也就是实际处理业务的handler
 */
public class HandlerMethod {
    // Controller的实例对象，比如：IndexController、UserController等
    private Object bean;

    // 所写的Controller的Class类型，比如：IndexController.class、UserController.class等
    private Class<?> beanType;

    // 表示Controller中的某个方法
    private Method method;

    /**
     * 方法中的所有参数的定义，这里引用了Spring中提供的`MethodParameter`工具类，
     * 里面封装了一些实用的方法，比如说后面会用到获取方法上的注解等等
     */
    private List<MethodParameter> parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;

        this.parameters = new ArrayList<>();
        int parameterCount = method.getParameterCount();
        for (int index = 0; index < parameterCount; index++) {
            this.parameters.add(new MethodParameter(method, index));
        }
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}
