package com.xdclass.springboot_test.controller;

import com.xdclass.springboot_test.domain.JsonData;
import com.xdclass.springboot_test.domain.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestExceptionController {
    @GetMapping("/testException")
    @ResponseBody
    public Object index() {
        int i = 1 / 0;
        return new JsonData(0, "HelloWorld", null);
    }

    @GetMapping("/testMyException")
    @ResponseBody
    public Object index2() {
        throw new MyException(499, "page not found!");
    }

}
