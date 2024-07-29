package com.scnu.smartmvc.hanler.returnvalue;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import com.scnu.smartmvc.view.View;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支持Handler直接返回需要渲染的`View`对象
 */
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return View.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer container, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 如果返回值是View对象，那么直接把视图放入到`ModelAndViewContainer`中
        if (returnValue instanceof View) {
            View view = (View) returnValue;
            container.setView(returnValue);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in ws: " + returnType.getMethod());
        }
    }
}
