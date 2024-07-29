package com.scnu.smartmvc.hanler.adapter;

import com.scnu.smartmvc.hanler.HandlerMethod;
import com.scnu.smartmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    /**
     * 把控制器中的方法的返回值需要封装成`ModelAndView`对象
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception;
}
