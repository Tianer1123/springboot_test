package com.xdclass.springboot_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ArgsController {
    Map<String, String> res = new HashMap<>();
    @GetMapping("/v1")
    @ResponseBody
    public Object getParams(@RequestParam String arg1, String arg2) {
        res.put("arg1", arg1);
        res.put("arg2", arg2);
        return res;
    }
}
