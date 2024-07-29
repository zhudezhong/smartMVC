package com.scnu.smartmvc.hanler.mapping;

import com.scnu.smartmvc.hanler.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;

/**
 * HandlerMapping组件主要作用是根据客户端的访问路径
 * 匹配到Controller处理器及对应的Method处理方法并将其包装在HandlerMethod兑现中
 * 用处理器执行链对象HandlerExecutionChain对象包装HandlerMethod，并在处理器执行链对象中添加拦截器。
 */
public interface HandlerMapping {

    /**
     * `HandlerMapping`接口中只有一个方法，
     * 通过request找到需要执行的handler，包装成`HandlerExecutionChain`，
     * @param request
     * @return
     * @throws Exception
     */
    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
