package com.scnu.smartmvc.view;

import com.scnu.smartmvc.BaseJunit4Test;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

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

}
