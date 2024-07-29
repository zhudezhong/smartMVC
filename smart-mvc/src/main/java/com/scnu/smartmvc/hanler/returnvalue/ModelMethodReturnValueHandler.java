package com.scnu.smartmvc.hanler.returnvalue;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return Model.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer container, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue == null) {
            return;
        } else if (returnValue instanceof Model) {
            container.getModel().addAllAttributes((Map) returnValue).asMap();
        } else {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                        returnType.getParameterType().getName() + " in ws: " + returnType.getMethod());
        }
    }
}
