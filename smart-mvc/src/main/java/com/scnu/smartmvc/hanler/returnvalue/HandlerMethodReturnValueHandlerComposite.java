package com.scnu.smartmvc.hanler.returnvalue;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer container, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
            if (handler.supportReturnType(returnType)) {
                handler.handleReturnValue(returnValue, returnType, container, request, response);
                return;
            }
        }
        throw new UnsupportedOperationException("Unexpected return type: " +
                returnType.getParameterType().getName() + " in ws: " + returnType.getMethod());
    }

    public List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        return returnValueHandlers;
    }

    public void clear() {
        this.returnValueHandlers.clear();
    }

    public void addReturnValueHandler(HandlerMethodReturnValueHandler... handlers) {
        Collections.addAll(this.returnValueHandlers, handlers);
    }

    public void addReturnValueHandler(List<HandlerMethodReturnValueHandler> handlers) {
        this.returnValueHandlers.addAll(handlers);
    }
}
