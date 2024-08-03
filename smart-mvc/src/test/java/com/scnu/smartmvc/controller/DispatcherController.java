package com.scnu.smartmvc.controller;

import com.scnu.smartmvc.annotation.*;
import com.scnu.smartmvc.exception.TestException;
import com.scnu.smartmvc.http.RequestMethod;
import com.scnu.smartmvc.result.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@ControllerAdvice
@Controller
@RequestMapping(path = "/test")
public class DispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {

        System.out.println("DispatcherController.dispatch: name=>" + name);
        model.addAttribute("name", name);

        return "redirect:/silently9527.cn";
    }

    @RequestMapping(path = "/dispatch2", method = RequestMethod.GET)
    public String dispatch2(@RequestParam(name = "name") String name) {

        System.out.println("DispatcherController.dispatch2: name=>" + name);

        throw new TestException("test Exception", name);
    }


    @ResponseBody
    @ExceptionHandler({TestException.class})
    public ApiResponse exceptionHandler(TestException exception) {
        System.out.println("exception message: " + exception.getMessage());
        return new ApiResponse(200, "exception handler complete", exception.getMessage());
    }

}
