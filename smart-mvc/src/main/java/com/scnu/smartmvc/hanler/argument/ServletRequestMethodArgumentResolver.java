package com.scnu.smartmvc.hanler.argument;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportParameter(MethodParameter parameter) {
        //在`supportsParameter`先取出Handler参数的类型，
        // 判断该类型是不是`ServletRequest`的子类，如果是返回true
        Class<?> parameterType = parameter.getParameterType();

        return ServletRequest.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {
        // 当`supportsParameter`返回true的时候执行`resolveArgument`方法，
        // 在该方法中直接返回request对象
        return request;
    }
}
