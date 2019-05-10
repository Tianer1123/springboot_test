package com.xdclass.springboot_test.controller;

import com.xdclass.springboot_test.domain.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class FileController {
    private static final String filePath = "D:\\Code\\springboot_test\\src\\main\\resources\\static\\images\\";

    @RequestMapping(value = "/upload")
    @ResponseBody
    public JsonData upload(@RequestParam("head_img")MultipartFile file, HttpServletRequest request) {
        // 获取form表单其他内容
        String name = request.getParameter("name");
        System.out.println("用户名：" + name);

        // 获取文件名
        String fileName = file.getOriginalFilename();
        System.out.println("上传文件名：" + fileName);

        // 文件上传后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传文件的后缀名：" + suffixName);

        // 上传后的文件路径
        fileName = UUID.randomUUID() + suffixName;
        System.out.println("转换后的名称:" + fileName);
        File dest = new File(filePath + fileName);

        try {
            // 文件存放到目的地
            file.transferTo(dest);
            return new JsonData(0, fileName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonData(-1, "fail to save file");
    }
}
