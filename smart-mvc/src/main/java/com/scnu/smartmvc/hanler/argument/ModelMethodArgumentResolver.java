package com.scnu.smartmvc.hanler.argument;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该解析器主要是从解析出Model对象，方便后期对Handler中的Model参数进行注入
 */
public class ModelMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {

        Assert.state(container != null, "ModelAndViewContainer is required for model exposure");

        return container.getModel();
    }


}
