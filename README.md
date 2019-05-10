# SpringBoot Http请求:

## 请求参数 

- **@PathVariable:**   路径参数 `/{arg1}/{arg2}` 方法中写法：`(@PathVariable("arg1") String arg1, @PathVarible("arg2") String arg2`。
- **@RequestParam:** `url`中 `?` 后面的参数 `http://localhost:8080/v1?id=1`
- **@RequestBody:** `body`
- **@RequestHeader:** `header`
- **HttpServletRequest request:** 自动注入获取参数



## jackson注解

- **@JsonIgnore:** 指定字段不反回，例如 `password` 。

- **@JsonFormat:** 指定日期格式。

  ```java
  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
  ```

- **@JsonInclude:** 空字段不反回。

  ```java
  @JsonInclude(Include.NON_NULL)
  ```

- **@JsonProperty:** 指定别名。



静态文件加载顺序：

```properties
META/resources > resources > static > public
```

[静态资源官方说明](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-static-content)：https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-static-content



*静态资源一般存放在CDN（内容分发网），前后端分离后，不会存放到后端服务器上。*



如果想在 `templates` 存放静态资源，需要添加模板引擎，例如：`themleaf` 等。否则直接存放到上面的静态资源路径下。



## 文件上传

`MultipartFile` 对象的 `transferTo` 用于保存文件。效率和操作比原先的FileOutStream方便和高效。