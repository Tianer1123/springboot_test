package com.xdclass.springboot_test.controller;

import com.xdclass.springboot_test.domain.JsonData;
import com.xdclass.springboot_test.domain.Person;
import com.xdclass.springboot_test.domain.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.xdclass.springboot_test.utils.ExcelUtils.importExcel;

@Controller
@RequestMapping
@PropertySource("classpath:application.properties")
public class FileController {
    // private static final String filePath = "D:\\Code\\springboot_test\\src\\main\\resources\\static\\images\\";
    // 读取配置文件中的值
    @Value("${web.images-path}")
    private String filePath;

    @RequestMapping(value = "/upload")
    @ResponseBody
    public JsonData upload(@RequestParam("head_img")MultipartFile file, HttpServletRequest request) {
        // file.isEmpty() 文件不能为空判断
        // file.getSize() 文件大小判断

        System.out.println("上传路径是：" + filePath);
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
        System.out.println("文件上传后的路径：" + filePath + fileName);

        try {
            // 文件存放到目的地
            file.transferTo(dest);
            return new JsonData(0, fileName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonData(-1, "fail to save file", fileName);
    }

    @Autowired
    ServerConfig serverConfig;

    @RequestMapping("/v2")
    @ResponseBody
    public Object getServer() {
        return serverConfig;
    }


    /**
     * 导入Excel
     * @param file 导入的Excel文件
     * @param request  httpRequest
     * @return
     */
    @RequestMapping(value = "/import")
    @ResponseBody
    public List importE(@RequestParam("excel")MultipartFile file, HttpServletRequest request) throws Exception {
        List<Person> personList = importExcel(file, 0, 1, Person.class);
        for (Person p: personList) {
            System.out.println(p.getName());
            System.out.println(p.getSex());
            System.out.println(p.getBirthday());
        }
        return personList;
    }
}
