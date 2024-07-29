package com.scnu.smartmvc.hanler.returnvalue;

import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支持Handler返回一个String对象，表示视图的路径
 */
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return CharSequence.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer container, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 如果返回值是String，那么把这个返回值当做是视图的名字，放入到`ModelAndViewContainer`中
        if (returnValue instanceof CharSequence) {
            String viewName = returnValue.toString();
            container.setView(viewName);
        } else if (returnValue != null){
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in ws: " + returnType.getMethod());
        }

    }
}
