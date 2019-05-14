# SpringBoot Http请求:

## 请求参数 

- **@PathVariable:**   路径参数 `/{arg1}/{arg2}` 方法中写法：`(@PathVariable("arg1") String arg1, @PathVarible("arg2") String arg2`。

- **@RequestParam:** `url`中 `?` 后面的参数 `http://localhost:8080/v1?id=1`

  ``` java
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
  ```

  请求连接：

  ``` http
  http://localhost:8080/v1?arg1=Hello&arg2=World
  ```

  返回结果：

  ``` json
  {"arg2":"World","arg1":"Hello"}
  ```

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



## 使用Maven打jar包

需要在 `pom` 文件中添加

``` xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

idea中在 Maven -> Lifecycle -> install 双击打jar包，生成路径在 target 目录下。



## @Value:

如果运行的是jar包，上传路径需要时服务器的本地路径，不能指定为jar包中的路径。配置方法如下：

``` properties
# application.properties  file:${web.images-path}:获取配置文件中的配置
web.images-path=/User/test/Desktop
spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/,file:${web.images-path}
```



``` java
@Controller
@PropertySource("classpath:application.properties")
public class FileController {
```

``` java
// 读取配置文件中的值
@Value("${web.images-path}")
private String filePath;
```



资源服务器和应用服务器分开处理：

fastdfs、阿里云oss、nginx自己搭建的简单文件服务器等等。



# 热部署dev和配置文件自动注入

## 热部署

[热部署文档](<https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#using-boot-devtools>)

**pom.xml中添加如下内容:**

``` xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>
</dependencies>
```

**idea的配置:**

1. Settings -> Build, Execution, Deployment -> Complier

   选择 Build project automatically.

2. ctrl+shift+alt+/ 到 registry

   选择 compiler.automake.allow.when.app.running



[修改某些文件不会热部署官方文档](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#using-boot-devtools-restart-exclude)

1. `/META-INF/maven`, `/META-INF/resources`, `/resources`, `/static`, `/public`, or `/templates`

2.  以下手动配置的路径下的文件修改后不会热启动

   ``` properties
   spring.devtools.restart.exclude=static/**,public/**
   ```

3. 使用触发文件触发，修改所有代码都不重启，只有通过一个文本grigger.txt去控制

   ``` properties
   spring.devtools.restart.trigger-file=trigger.txt
   ```

   

## 配置文件

[common配置文件，用到哪些copy哪些。](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#common-application-properties)



## 配置文件的自动注入

1. @PropertySource加载配置文件，在变量前添加@Value，自动注入给变量。

2. 通过实体类注入配置信息：

   ``` java
   // 实体类的设置，类属性名称与配置文件一致
   @Component
   @PropertySource("classpath:application.properties")
   @ConfigurationProperties(prefix = "test")
   public class ServerConfig {
       // 类属性名要与配置文件一致，不需要加@Value()注解。
       String name;
       String domain;
   }
   ```

   ``` properties
   # application.properties中的配置
   test.name=spring-test
   test.domain=www.spring-test.com
   ```

   ``` java
   // Controller 中的使用
   @Autowired
   ServerConfig serverConfig;
   
   @RequestMapping("/v2")
   @ResponseBody
   public Object getServer() {
       return serverConfig;
   }
   ```

   访问:http://localhost:8080/v2 返回的结果是：

   ``` json
   {
   	name: "spring-test",
   	domain: "www.spring-test.com",
   }
   ```




# Oracle分页查询

``` sql
-- 这种写反比较高效
SELECT * FROM  
(  
SELECT A.*, ROWNUM RN  
FROM (SELECT * FROM TABLE_NAME) A  
WHERE ROWNUM <= 40  
)  
WHERE RN >= 21  
```

等同于下面的写法

``` sql
-- 这种写法简单，但是效率比较低
SELECT * FROM  
(  
SELECT A.*, ROWNUM RN  
FROM (SELECT * FROM TABLE_NAME) A  
)  
WHERE RN BETWEEN 21 AND 40
```





# 单元测试与自定义异常

## 单元测试

``` java
 @Test
    public void contextLoads() {
        System.out.println("Hello!");
        TestCase.assertEquals(1, 1);
    }

    @Test
    public void contextLoads2() {
        System.out.println("contextLoads2");
    }

    // test之前调用
    @Before
    public void testBefroe() {
        System.out.println("before");
    }

    // test之后调用
    @After
    public void testAfter() {
        System.out.println("after");
    }
```

## MockMvc模拟http请求

测试API接口使用`MockMvc`模拟请求：

``` java
@RunWith(SpringRunner.class)
@SpringBootTest
// 需要添加这个注解
@AutoConfigureMockMvc
public class SpringbootTestApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testArgsController() throws Exception{
        // perform模拟一个http请求
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1?arg1=1&arg2=2")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
```

## 设置banner

[设置banner官方文档](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-banner)

1. 创建 `banner.txt`文件
2. 修改配置`spring.banner.location=banner.txt`

## Debug日志打印

``` shell
java -jar *.jar --debug
```

## 自定义异常处理1

[自定义异常处理官网文档](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-error-handling)

``` java
// 返回json串，如果使用@ControllerAdvice注解，需要添加@ReponseBody注解
//@ControllerAdvice
@RestControllerAdvice
public class CustomExceptionHandler {
    // 获取所有的异常信息
    @ExceptionHandler(value = Exception.class)
    // @ResponseBody
    Object handleException(Exception e, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("msg", e.getMessage());
        map.put("url", request.getRequestURL());
        return map;
    }
}
```

测试用的controller：

``` java
@Controller
public class TestExceptionController {
    @GetMapping("/testException")
    @ResponseBody
    public Object index() {
        int i = 1 / 0; // 这里会报异常
        return new JsonData(0, "HelloWorld", null);
    }
}
```

返回的结果：

``` json
{
    msg: "/ by zero",
    code: 100,
    url: "http://localhost:8080/testException",
}
```

如果不自定义这些信息，返回的异常信息将非常不友好。



## 自定义异常处理2

``` java
// 自定义异常类
public class MyException extends RuntimeException{
    int code;
    String msg;

    public MyException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```



`CustomExceptionHandler` 中添加自定义的异常处理函数

``` java
@ExceptionHandler(value = MyException.class)
    Object handleMyException(MyException e, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("msg", e.getMsg());
        map.put("url", request.getRequestURL());
        return map;
    }
```



controller中抛出自定义异常：

``` java
    @GetMapping("/testMyException")
    @ResponseBody
    public Object index2() {
        throw new MyException(499, "page not found!");
    }
```

# War包部署

使用 `JMeter` 做性能测试。

``` properties
	<!-- 打jar包 -->
	<packaging>jar</packaging>
	<!-- ... -->
```

``` properties
	<!-- 打war包 -->
	<packaging>war</packaging>
	<!-- ... -->
```

idea中在 Maven -> Lifecycle -> clean/install 双击打jar/war包，生成路径在 target 目录下。打war包的main函数与jar包不同，需要注意一下。



``` java
// jar包的main函数
@SpringBootApplication
public class SpringbootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

}
```

``` java
// war包的main函数
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

}

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

# 什么是javaBean

**avaBean是一个遵循特定写法的Java类**，它通常具有如下特点：

1. 这个Java类必须具有一个无参的构造函数。
2. 属性必须私有化。
3. 私有化的属性必须通过public类型的方法暴露给其它程序，并且方法的命名也必须遵守一定的命名规范。

# Filter过滤器

> 举个例子: 人-->检票员（filter）-->景点

Servlet3.0提供新的自定义Filter的方式：

1. 使用Servlet3.0注解进行配置。

2. 启动类中添加 `@ServletComponentScan` 进行扫描。

3. 新建一个Filter类， `implements Filter` , 实现对应的接口。

4. `@WebFilter` 标记一个类为filter，被spring进行扫描。

   ``` java
    urlPatterns // 拦截规则，支持正则
   ```

5. 放行和拦截

   ``` java
   chain.doFilter; // 放行
   resp.sendRedirect("/index.html"); // 拦截跳转
   ```



完整代码：

``` java
// 启动类
@SpringBootApplication
@ServletComponentScan
public class SpringbootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

}
```

``` java
// 自定义过滤器
@WebFilter(urlPatterns = "/api/*", filterName = "MyFilter")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init MyFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, 
                         ServletResponse servletResponse, 
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter doFilter");
        HttpServletRequest res = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;
        String userName = res.getParameter("userName");
        if ("tianer".equals(userName)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            rep.sendRedirect("/index.html");
            return;
        }

    }

    @Override
    public void destroy() {
        System.out.println("destroy MyFilter");
    }
}
```

```java
// controller实现
@GetMapping("/api/testFilter")
@ResponseBody
public Object testFilter(@RequestParam String userName, String passWord) {
    res.put("userName", userName);
    res.put("passWord", passWord);
    return res;
}
```

``` http
// 访问连接
http://localhost:8080/api/testFilter?userName=tianer&passWord=123
```

``` json
// 返回结果
{
    passWord: "123",
    userName: "tianer",
}
```



