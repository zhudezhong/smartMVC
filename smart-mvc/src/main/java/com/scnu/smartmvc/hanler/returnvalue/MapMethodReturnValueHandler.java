package com.scnu.smartmvc.hanler.returnvalue;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 支持Handler返回Map值，放入到上下文中，用于页面渲染使用
 */
public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    /**
     * 同参数解析器一样，判断处理器是否支持该返回值为Map类型
     *
     * @param returnType
     * @return
     */
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Map.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof Map) {
            mavContainer.getModel().addAllAttributes((Map) returnValue);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }

    }
}
