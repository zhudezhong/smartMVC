package com.scnu.smartmvc.hanler.returnvalue;

import com.alibaba.fastjson.JSON;
import com.scnu.smartmvc.annotation.ResponseBody;
import com.scnu.smartmvc.hanler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当方法或者Controller被注解`@ResponseBody`标注时，返回值需要被转换成JSON字符串输出
 */
public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    /**
     * 判断当前方法或者Controller 有没有被 注解`@ResponseBody`标注
     *
     * @param returnType
     * @return
     */
    @Override
    public boolean supportReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ResponseBody.class);
    }

    /**
     * 当方法或者Controller被注解`@ResponseBody`标注时，返回值需要被转换成JSON字符串输出
     *
     * @param returnValue Handler执行之后的返回值
     * @param returnType
     * @param container
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer container, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 标记本次请求已经处理完成
        container.setRequestHandled(true);

        outPutMessage(response, JSON.toJSONString(returnValue));
    }

    private void outPutMessage(HttpServletResponse response, String message) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            out.write(message);
        }

    }
}
