package com.scnu.smartmvc.hanler.argument;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportParameter(MethodParameter parameter) {
        //在`supportsParameter`先取出Handler参数的类型，
        // 判断该类型是不是`ServletRequest`的子类，如果是返回true
        Class<?> parameterType = parameter.getParameterType();

        return ServletResponse.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {
        return response;
    }
}
