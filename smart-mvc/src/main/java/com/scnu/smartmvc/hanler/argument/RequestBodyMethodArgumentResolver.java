package com.scnu.smartmvc.hanler.argument;

import com.alibaba.fastjson.JSON;
import com.scnu.smartmvc.annotation.RequestBody;
import com.scnu.smartmvc.exception.MissingServletRequestParameterException;
import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    /**
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
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        String httpMessageBody = this.getHttpMessageBody(request);
        if (!StringUtils.isEmpty(httpMessageBody)) {
            // 把取出来的字符串通过fastjson转换成参数类型的对象
            return JSON.parseObject(httpMessageBody, parameter.getParameterType());
        }

        RequestBody param = parameter.getParameterAnnotation(RequestBody.class);
        if (Objects.isNull(param)) {
            return null;
        }

        if (param.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getTypeName());
        }

        return null;

    }

    private String getHttpMessageBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buff = new char[1024];
        int len;
        while ((len = reader.read(buff)) != -1) {
            sb.append(buff, 0, len);
        }

        return sb.toString();
    }
}
