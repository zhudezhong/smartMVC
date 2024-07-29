package com.scnu.smartmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestInvocableHandlerMethodController {
    public void testRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {

        Assert.notNull(request, "request can not null");
        Assert.notNull(response, "response can not null");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String name = request.getParameter("name");
            writer.println("Hello InvocableHandlerMethod, params: " + name);

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) writer.close();
        }

    }

    public String testViewName(Model model) {
        model.addAttribute("blogURL", "http://silently9527.cn");
        return "/silently9527.jsp";
    }
}
