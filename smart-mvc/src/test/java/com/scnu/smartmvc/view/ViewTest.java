package com.scnu.smartmvc.view;

import com.scnu.smartmvc.BaseJunit4Test;
import com.scnu.smartmvc.utils.RequestContextHolder;
import com.scnu.smartmvc.view.resolver.ContentNegotiatingViewResolver;
import com.scnu.smartmvc.view.resolver.InternalResourceViewResolver;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ViewTest extends BaseJunit4Test {

    /**
     * 9、测试 RedirectView 功能
     */
    @Test
    public void test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        HashMap<String, Object> model = new HashMap<>();
        model.put("name", "zhangsan");
        model.put("url", "http://localhost");

        RedirectView redirectView = new RedirectView("/redirect/login");
        redirectView.render(model, request, response);

        response.getHeaderNames().forEach(headerName -> {
            System.out.println(headerName + "：" + response.getHeader(headerName));
        });
    }


    /**
     * 10、测试视图解析器
     */
    @Test
    public void test2() throws Exception {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", "text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = negotiatingViewResolver.resolveViewName("redirect:/silently9527.cn");
        Assert.assertTrue(redirectView instanceof RedirectView); // 判断是否返回重定向视图

        View forwardView = negotiatingViewResolver.resolveViewName("forward:/silently9527.cn");
        Assert.assertTrue(forwardView instanceof InternalResourceView);

        View view = negotiatingViewResolver.resolveViewName("/silently9527.cn");
        Assert.assertTrue(view instanceof InternalResourceView); // 通过头信息‘Accept’, 判断是否返回‘InternalResourceView’


    }

}
