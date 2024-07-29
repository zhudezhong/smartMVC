package com.scnu.smartmvc.hanler.returnvalue;


import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 返回值的处理器
 */
public interface HandlerMethodReturnValueHandler {

    /**
     *  同参数解析器一样，判断处理器是否支持该返回值的类型
     * @param returnType
     * @return
     */
    boolean supportReturnType(MethodParameter returnType);

    /**
     *
     * @param returnValue  Handler执行之后的返回值
     * @param returnType
     * @param container
     * @param request
     * @param response
     * @throws Exception
     */
    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer container,
                           HttpServletRequest request, HttpServletResponse response) throws Exception;

}
