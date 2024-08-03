package com.scnu.smartmvc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class DispatcherServletTest extends BaseJunit4Test {

    @Autowired
    DispatcherServlet dispatcherServlet;

    /**
     * 11、测试DispatcherServlet
     */
    @Test
    public void test() throws Exception {
        System.out.println(dispatcherServlet);
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "zhangsan"); // 设置请求的参数name
        request.setRequestURI("/test/dispatch"); // 设置请求的URI

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        System.out.println("response: ");
        response.getHeaderNames().forEach(headerNames -> {
            System.out.println(headerNames + ":" + response.getHeader(headerNames));
        });

    }

    @Test
    public void test2() throws ServletException, IOException {
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "zhangsan");
        request.setRequestURI("/test/dispatch2");

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        System.out.println("响应到客户端的数据：");
        System.out.println(response.getContentAsString());

    }

}
