package com.scnu.smartmvc.hanler.argument;


import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 参数解析器的组合类
 * <p>
 * 该类同样也实现接口`HandlerMethodArgumentResolver`，内部定义List，
 * 在`resolveArgument`中循环所有的解析器，找到支持参数的解析器就开始解析，找不到就抛出异常
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {
    private List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {

        for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
            if (resolver.supportParameter(parameter)) {
                return resolver.resolveArgument(parameter, request, response, container, conversionService);
            }
        }
        throw new IllegalArgumentException("Unsupported parameter type [" +
                parameter.getParameterType().getName() + "]. supportsParameter should be called first.");

    }

    public void addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
    }

    public void addResolver(HandlerMethodArgumentResolver... resolvers) {
        Collections.addAll(this.argumentResolvers, resolvers);
    }

    public void addResolver(List<HandlerMethodArgumentResolver> resolvers) {
        this.argumentResolvers.addAll(resolvers);
    }

    public void clear() {
        this.argumentResolvers.clear();
    }

}
