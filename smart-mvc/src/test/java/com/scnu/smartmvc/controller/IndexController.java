package com.scnu.smartmvc.controller;

import com.scnu.smartmvc.annotation.RequestMapping;
import com.scnu.smartmvc.http.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(path = "/index")
public class IndexController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public void test() {
    }

    @RequestMapping(path = "/test2", method = RequestMethod.POST)
    public void test2() {
    }

    public void test3() {
    }
}
