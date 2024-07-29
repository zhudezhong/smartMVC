package com.scnu.smartmvc.hanler.argument;

import com.scnu.smartmvc.annotation.RequestParam;
import com.scnu.smartmvc.exception.MissingServletRequestParameterException;
import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 注解 @RequestParam 的参数解析功能
 * <p>
 * 当Handler中的参数被`@RequestParam`标注，需要从request中取出对应的参数，
 * 然后调用`ConversionService`转换成正确的类型。
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断 Handler 的参数是否有添加注解 `@RequestParam`
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    /**
     * 从request中找指定name的参数，如果找不到用默认值赋值，如果默认值也没有，当required=true时抛出异常，
     * 否则返回null; 如果从request中找到了参数值，那么调用`conversionService.convert`方法转换成正确的类型
     *
     * @param parameter
     * @param request
     * @param response
     * @param container
     * @param conversionService
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {

        RequestParam param = parameter.getParameterAnnotation(RequestParam.class);
        if (Objects.isNull(param)) {
            return null;
        }

        String value = request.getParameter(param.name());
        if (StringUtils.isEmpty(value)) {
            value = param.defaultValue();
        }

        if (!StringUtils.isEmpty(value)) {
            return conversionService.convert(value, parameter.getParameterType());
        }

        if (param.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }

        return null;
    }
}
