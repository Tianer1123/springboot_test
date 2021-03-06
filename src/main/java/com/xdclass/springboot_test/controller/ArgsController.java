package com.xdclass.springboot_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ArgsController {
    private Map<String, String> res = new HashMap<>();
    @GetMapping("/v1")
    @ResponseBody
    public Object getParams(@RequestParam String arg1, String arg2) {
        res.put("arg1", arg1);
        res.put("arg2", arg2);
        return res;
    }

    @GetMapping("/api/testFilter")
    @ResponseBody
    public Object testFilter(@RequestParam String userName, String passWord) {
        res.put("userName", userName);
        res.put("passWord", passWord);
        return res;
    }

    @GetMapping("/api2/test")
    @ResponseBody
    public Object testInterceptor(@RequestParam String userName, String passWord) {
        res.put("userName", userName);
        res.put("passWord", passWord);
        return res;
    }
}
