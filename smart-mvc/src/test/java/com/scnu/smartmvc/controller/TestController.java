package com.scnu.smartmvc.controller;

import com.scnu.smartmvc.annotation.RequestBody;
import com.scnu.smartmvc.annotation.RequestMapping;
import com.scnu.smartmvc.annotation.RequestParam;
import com.scnu.smartmvc.http.RequestMethod;
import com.scnu.smartmvc.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TestController {

    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(@RequestParam(name = "name") String name,
                      @RequestParam(name = "age") Integer age,
                      @RequestParam(name = "birthday") Date birthday,
                      HttpServletRequest request) {
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public void user(@RequestBody UserVO userVO) {

    }
}
