package com.scnu.smartmvc.controller;

import com.scnu.smartmvc.annotation.RequestBody;
import com.scnu.smartmvc.annotation.ResponseBody;
import com.scnu.smartmvc.view.RedirectView;
import com.scnu.smartmvc.view.View;
import com.scnu.smartmvc.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestReturnValueController {


    @ResponseBody
    public UserVO testResponseBody() {
        UserVO userVO = new UserVO();
        userVO.setBirthday(new Date());
        userVO.setAge(22);
        userVO.setName("wangwu");
        return userVO;
    }

    public String testViewName() {
        return "/jsp/index.jsp";
    }

    public View testView() {
        return new RedirectView("/jsp/logout.jsp");
    }

    public Map<String, Object> testMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("testMap", "zhudezhong");
        return map;
    }

    public Model testModel(Model model) {
        model.addAttribute("testModel", "zhudezhong");
        return model;
    }

}
