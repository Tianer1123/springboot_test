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

# @RestController

`@RestController：` 返回json格式数据。

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

2. windows: ctrl+shift+alt+/ 到 registry

   选择 compiler.automake.allow.when.app.running
   
   Mac: command+shift+a ，输入 registry



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

   ``` properties
   <!--   使用自己的配置信息     -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-configuration-processor</artifactId>
       <optional>true</optional>
   </dependency>
   ```
   
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
-- 这种写法比较高效
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

**JavaBean是一个遵循特定写法的Java类**，它通常具有如下特点：

1. 这个Java类必须具有一个无参的构造函数。
2. 属性必须私有化。
3. 私有化的属性必须通过public类型的方法暴露给其它程序，并且方法的命名也必须遵守一定的命名规范。

# Filter, Servlet, Listener

## Filter过滤器

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

## 自定义原生servlet

servlet3.0自定义原生servlet

``` java
// 启动函数需要加一下注解，与过滤器一样
@ServletComponentScan
```



``` java
@WebServlet(name = "userServlet", urlPatterns = "/api/v1/test/do")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("user servlet");
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
```

## Listener

常用的三个listener: `ServletRequestListener`,`ServletContextListener`,`HttpSessionListener`



``` java
@WebListener
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("========requestDestroyed========");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("========requestInitialized========");
    }
}
```



[Servlets, Filters, Listeners官方文档介绍](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-embedded-container-servlets-filters-listeners)



## 拦截器

SpringBoot2.x拦截器和旧版本拦截器区别：

1. @Configuration
   * SpringBoot旧版本： 继承自 `extends WebConfigurationAdapter`
   * SpringBoot2.x 配置拦截器：`implements WebMvcConfigurer`

2. 自定义拦截器 HandlerInterceptor
   * preHandle: Controller调用之前。
   * postHandle: Controller之后调用，如果Controller出现异常，不执行吃方法。
   * afterCompletion: 无论是否有异常，该方法都会被执行，用于资源清理。
3. 按照注册顺序进行拦截，先注册，先拦截。



**拦截器不生效的常见问题：**

1. 是否加了注解 `@Configuration`
2. 拦截器路径是否有问题 ** 和 *
3. 拦截器最后路径一定要 "/**"，如果是目录则是 /\*/



具体代码实例：

``` java
// 自定义一个config文件添加拦截器

@Configuration
public class MyConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/api2/*/**");
//      添加多个拦截器
//      registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/api2/*/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
```



``` java
// 自定义拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=====>LoginInterceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("=====>LoginInterceptor postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("=====>LoginInterceptor afterCompletion");
    }
}
```

``` java
// controller测试
@GetMapping("/api2/test")
@ResponseBody
public Object testInterceptor(@RequestParam String userName, String passWord) {
    res.put("userName", userName);
    res.put("passWord", passWord);
    return res;
}
```

``` http
http://localhost:8080//api2/test?userName=1&passWord=123
```

``` verilog
2019-05-15 14:14:39.625  INFO 1970 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-05-15 14:14:39.625  INFO 1970 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-05-15 14:14:39.632  INFO 1970 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 7 ms
=====>LoginInterceptor preHandle
=====>LoginInterceptor postHandle
=====>LoginInterceptor afterCompletion
========requestDestroyed========
========requestInitialized========
========requestDestroyed========
2019-05-15 14:14:59.304  INFO 1970 --- [      Thread-23] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
destroy MyFilter
```



## 过滤器和拦截器的区别

Spring的拦截器与Servlet的Filter有相似之处，比如都能实现权限检查、日志记录等。不同的是：

* Filter是基于函数回调 `doFilter()` ，而Interceptor则是基于AOP思想编程。

* 使用范围不同：Filter是Servlet规范规定的，只能用于Web程序中。而拦截器既可以用于Web程序，也可以用于Application、Swing程序中。
* 规范不同：Filter是在Servlet规范中定义的，是Servlet容器支持的。而拦截器是在Spring容器内的，是Spring框架支持的。
* 使用的资源不同：同其他的代码块一样，拦截器也是一个Spring的组件，归Spring管理，配置在Spring文件中，因此能使用Spring里的任何资源、对象。例如Service对象、资源对象、事务管理等，通过IoC注入到拦截器即可；而Filter则不能。
* 深度不同：Filter只在Servlet前后起作用。而拦截器能够深入到方法前后、异常抛出前后等，因此拦截器的使用具有更大的弹性。

所以在Spring架构的程序中，要优先使用拦截器。

**Filter和Interceptor的执行顺序**

过滤前->拦截前->action执行->拦截后->过滤后



# starter

[starter官网讲解](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#using-boot-starter)

**功能：** 通过pom.xml添加starter来简化依赖。

`pom.xml` 常用的starter:

``` properties
spring-boot-starter-web
spring-boot-starter-data-redis 
spring-boot-starter-jdbc
spring-boot-starter-data-mongodb
spring-boot-starter-thymeleaf
spring-boot-starter-freemarker
```



# 模板引擎

## jsp（后端渲染，消耗性能）

JSP(Java Server Pages) 动态网页技术，有服务器的JSP引擎来编译和执行，再讲生成的整个页面返回给客户端。

优缺点：

* 可以使用Java代码编写。
* 支持表达式语言(el，jstl)
* 内建函数
* JSP->Servlet(占用JVM内存)
* javaweb官方推荐
* Springboot不推荐

## freemarker

FreeMarker Template Language(FTL) 文件一般保存为 xxx.ftl

* 严格依赖MVC模式，不依赖Servlet容器(不占用JVM内存)
* 内建函数

## thymeleaf(主推)

轻量级的模板引擎(负责复杂业务逻辑的不推荐，解析DOM或者XML会占用比较多的内存)

* 可以直接在浏览器打开且显示正确的模板页面
* 直接是以.html结尾，可以直接编辑

## 使用freemarker

``` xml
<!--freemarker模板引擎-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

``` properties
# 是否开启freemarker缓存，本地为false，生产建议为true
spring.freemarker.cache=false

spring.freemarker.charset=utf-8
spring.freemarker.allow-request-override=false
spring.freemarker.check-template-location=true

# 类型
spring.freemarker.content-type=text/html

spring.freemarker.expose-request-attributes=true
spring.freemarker.expose-session-attributes=true

# 文件后缀
spring.freemarker.suffix=.ftl
# 路径
spring.freemarker.template-loader-path=classpath:/templates/
```

# 数据库访问持久化

## 访问数据库的几种方式

1. 原始的SQL：

   缺点是：开发流程麻烦。

   1. 注册驱动/加载驱动。

      ``` java
      Class.forName("com.mysql.jdbc.Driver");
      ```

   2. 建立连接。

      ``` java
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "root", "root");
      ```

   3. 创建Statement。

   4. 执行SQL语句。

   5. 处理结果集。

   6. 关闭连接，释放资源。

2. apache dbutils框架

   比原始的SQL方式简单一点。

   [apache dbutils官网。](https://commons.apache.org/proper/commons-dbutils/)

3. jpa框架

   Spring-data-jpa

   jpa在复杂的查询的时候性能不太好。

4. Hiberante框架

   **ORM：** 对象关系映射，Object Relational Mapping

   企业大部分使用Hiberante框架。

5. Mybatis框架

   互联网企业大部分使用Mybatis框架。

   半ORM框架，不提供对象和关系的直接映射。

## 配置mybatis

添加mybatis的相关依赖。

``` xml
<!--Mybatis配置:Mybatis,mysql,druid-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.8</version>
</dependency>
```

## 定义User类

``` java
@Data
public class User {
    private int id;
    private String name;
    private String phone;
    private int age;
    private Date createTime;
}
```

``` sql
-- sql
create database xdclass;
create table `user` (
    `id` int(11) unsigned not null auto_increment,
    `name` varchar(128) default null comment '名称',
    `phone` varchar(16) default null comment '用户手机号',
    `create_time` datetime default null comment '创建时间',
    `age` int(4) default null comment '年龄',
    PRIMARY KEY (`id`)
) engine=InnoDB AUTO_INCREMENT=18 default charset=utf8;
```

``` properties
# 数据源和mybatis配置
spring.datasource.url=jdbc:mysql://localhost:3306/xdclass?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=root
```

启动类中添加注解

``` java
@MapperScan("com.xdclass.springboot_test.mapper")
```

Mapper中：

``` java
@Mapper
@Component
public interface UserMapper {
    // 使用#{},不要使用${},因为存在注入风险
    @Insert("INSERT INTO user(NAME, PHONE, CREATE_TIME, AGE) VALUES (#{name}, #{phone}, #{create_time}, #{age})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int inseart(User user);
}
```

Service实现：

``` java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public int add(User user) {
        mapper.inseart(user);
        int id = user.getId();
        return id;
    }
}
```

Controller实现

``` java
// RestController 返回json对象
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping(value = "/add")
    public Object add() {
        User user = new User();
        user.setAge(11);
        user.setCreateTime(new Date());
        user.setName("xdClass");
        user.setPhone("100001110");
        int id = service.add(user);
        return user;
    }
}
```

## Mybatis控制台打印SQL语句

``` properties
# 使用阿里巴巴druid数据源，默认使用自带的
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

# 开启控制台打印sql
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

## 事务，隔离级别，传播行为

**事务** ： 单机事务，分布式事务

**隔离级别** ： 

- Serializable: 最严格，串行处理，消耗资源大。
- Repeatable Read: 保证一个事务不会修改已经有另一个事务读取但未提交(回滚)的数据。
- Read Committed: 大多数主流数据库的默认事务登记
- Read Uncommitted: 保证读取过程中不会读取到非法数据。

**传播行为：**

- PROPAGATION_REQUIRED: 支持当前事务，如果当前没有事务，就新建一个事务，
- PROPAGATION_SUPPORTS: 支持当前事务，如果当前没有事务，就以非事务方式执行。
- PROPAGATION_MANDATORY: 支持当前事务，如果当前没有事务，抛异常。
- PROPAGATION_REQUIRES_NEW:  支持当前事务，如果当前没有事务，就把当前事务挂起。两个事务之间没有关系，一个异常，一个提交，不会同时回滚。
- PROPAGATION_NOT_SUPPORTED: 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- PROPAGATION_NEVER: 以非事务方式执行，如果当前存在事务，则抛异常。

**事务注解：**

```java
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
```

# Lombok插件

生成`Setter`，`Getter`插件。

``` xml
<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.6</version>
    <scope>provided</scope>
</dependency>
```

## Lombok的@Accessors是什么作用

Accessor的中文含义是存取器，`@Accessors`用于配置`getter`和`setter`方法的生成结果。

### **fluent**

fluent的中文含义是流畅的，设置为true，则getter和setter方法的方法名都是基础属性名，且setter方法返回当前对象。如下：

``` java
@Data
@Accessors(fluent = true)
public class User {
    private Long id;
    private String name;
    
    // 生成的getter和setter方法如下，方法体略
    public Long id() {}
    public User id(Long id) {}
    public String name() {}
    public User name(String name) {}
}
```

### **chain**

chain的中文含义是链式的，设置为true，则setter方法返回当前对象。如下:

``` java
@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    
    // 生成的setter方法如下，方法体略
    public User setId(Long id) {}
    public User setName(String name) {}
}
```

### **prefix**

prefix的中文含义是前缀，用于生成getter和setter方法的字段名会忽视指定前缀（遵守驼峰命名）。如下:

``` java
@Data
@Accessors(prefix = "p")
class User {
	private Long pId;
	private String pName;
	
	// 生成的getter和setter方法如下，方法体略
	public Long getId() {}
	public void setId(Long id) {}
	public String getName() {}
	public void setName(String name) {}
}
```



# 数据格式校验注解

## 为空校验

1. @Null   验证对象是否为**null**  
2. @NotNull验证对象是否不为**null**, 无法查检长度为0的字符串  
3. @NotBlank 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.  
4. @NotEmpty 检查约束元素是否为NULL或者是EMPTY，用在集合类上面

## **Booelan检查**

1. @AssertTrue 验证 Boolean 对象是否为 **true**   
2. @AssertFalse验证 Boolean 对象是否为 **false**  

## **长度检查**

1. @Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内。可以验证集合内元素的多少。  
2. @Length(min=, max=) Validates that the annotated string is between min and max included.主要用于String类型

## 日期检查

1. @Past   验证 Date 和 Calendar 对象是否在当前时间之前    
2. @Future 验证 Date 和 Calendar 对象是否在当前时间之后    
3. @Pattern验证 String 对象是否符合正则表达式的规则

## **数值检查**

1. 
   @Min            验证 Number 和 String 对象是否大等于指定的值    
2. @Max            验证 Number 和 String 对象是否小等于指定的值    
3. @DecimalMax 被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度  
4. @DecimalMin 被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度  
5. @Digits     验证 Number 和 String 的构成是否合法    
6. @Digits(integer=,fraction=) 验证字符串是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。

## **其他**

1. @Valid 递归的对关联对象进行校验, 如果关联对象是个集合或者数组,那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验.(是否进行递归验证)  
2. @CreditCardNumber信用卡验证  
3. @Email  验证是否是邮件地址，如果为**null**,不进行验证，算通过验证。  
4. @ScriptAssert(lang= ,script=, alias=)  
5. @URL(protocol=,host=, port=,regexp=, flags=)  
6. @Range(min=, max=) Checks whether the annotated value lies between (inclusive) the specified minimum and maximum.  
7. @Range(min=10000,max=50000,message="range.bean.wage")  

# Redis

pom.xml

``` xml
<!--redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

application.properties

``` properties
# redis基础配置
spring.redis.database=0
spring.redis.host=192.168.3.221
spring.redis.port=6379
# 连接超时时间，单位ms
spring.redis.timeout=3000

# redis连接池配置
# 连接池中最大空闲连接，默认是8
spring.redis.jedis.pool.max-idle=200
# 连接池中最小空闲连接，默认是0
spring.redis.jedis.pool.min-idle=200
# 等待可用连接的最大时间，单位ms，默认值-1，表示用不超时
spring.redis.jedis.pool.max-wait=1000
# -1表示不限制，pool已经分配了MaxActive个Jedis的实例，此时pool的状态时已耗尽。
spring.redis.jedis.pool.max-active=2000
```

简单测试：Controller

``` java
@Autowired
private StringRedisTemplate redisTpl;
@GetMapping(value = "/redis/add")
public Object redisAdd() {
    redisTpl.opsForValue().set("name", "lilei");
    return "OK";
}

@GetMapping(value = "/redis/get")
public Object redisGet() {
    String value = redisTpl.opsForValue().get("name");
    return value;
}
```

# SpringBoot定时任务和异步任务处理

## 定时任务 schedule

常见定时任务：

1. Java自带的定时任务 `java.util.Timer` 类。不推荐。

   * timer:配置麻烦，时间延后。
   * timerTask:

2. Quartz框架。

   * 配置简单。
   * xml或者注解。

3. SpringBoot注解方式：

   * 在启动类中添加@EnableScheduling定时任务，自动扫描。

   * 定时任务业务类添加 @Component被容器扫描。
   * 定时任务方法中加 @Scheduled(fixedRate = 2000) 定时执行方法。

   

代码实现：

``` java
@Component
public class TestTask {
    @Scheduled(fixedRate = 2000) // 两秒执行一次任务
    public void test() {
        System.out.println("当前时间为：" + new Date());
    }
}
```



## 定时任务配置

1. cron 定时任务表达式 `@Scheduled(cron="*/1 * * * * *")` 表示每秒。
2. fixedrate: 定时多久执行一次(上一次 **开始执行** 点后2秒)。
3. fixedDelay: 上一次 **执行结束** 时间点后2秒再次执行。
4. fixedDelayString: 字符串形式，可以通过配置文件制定。



## 异步任务

1. 使用场景： log、发邮件、短信……等。

2. 启动类里面添加 `@EnableAsync` 注解开启功能，自动扫描。

3. 定义异步任务类并使用 `@Component` 标记组件被容器扫描，异步方法加上 `@Async`。

   注意点：

   1. 把异步任务封装到类中，不要写到controller中。
   2. 增加`Future<String> ` 返回结果 `AsyncResult<String>("task执行完成")`。
   3. 如果需要拿到结果，需要判断全部的 `task.isDone()`。

4. 通过注入的方式，注入到controller中，如果测试前后区别，将@Async注释掉。

实例代码：

``` java
@Component
@Async   // 可以加到类上或者方法上
public class AsyncTask {

    public void task1 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(1000L);
        long end = System.currentTimeMillis();
        System.out.println("任务1耗时=" + (end - begin));
    }

    public void task2 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(2000L);
        long end = System.currentTimeMillis();
        System.out.println("任务2耗时=" + (end - begin));
    }

    public void task3 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(3000L);
        long end = System.currentTimeMillis();
        System.out.println("任务3耗时=" + (end - begin));
    }

    // 返回异步结果
    public Future<String> task4 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(2000L);
        long end = System.currentTimeMillis();
        System.out.println("任务4耗时=" + (end - begin));
        return new AsyncResult<String>("任务4");
    }

    public Future<String> task5 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(3000L);
        long end = System.currentTimeMillis();
        System.out.println("任务5耗时=" + (end - begin));
        return new AsyncResult<String>("任务5");
    }

    public Future<String> task6 () throws InterruptedException {
        long begin = System.currentTimeMillis();
        Thread.sleep(1000L);
        long end = System.currentTimeMillis();
        System.out.println("任务6耗时=" + (end - begin));
        return new AsyncResult<String>("任务6");
    }
}
```

``` java
@Autowired
private AsyncTask task;
@GetMapping(value = "/task")
public Object exeTask() throws Exception {
    long begin = System.currentTimeMillis();
//        task.task1();
//        task.task2();
//        task.task3();
    Future<String> task4 = task.task4();
    Future<String> task5 = task.task5();
    Future<String> task6 = task.task6();

    for (;;) {
        if (task4.isDone() && task5.isDone() && task6.isDone()) {
            break;
        }
    }
    long end = System.currentTimeMillis();
    long total = end - begin;
    System.out.println("完成：" + total);
    return total;
}
```

# Logback日志框架

SpringBoot为什么推荐使用`Logback` ，有的日志框架性能太低，日志阻塞，导致QPS（query per second 每秒查询率）上不去。



1） **常用的日志框架：** `slf4j`，`log4j`，`logback`，`common-logging`等。

2） **logback介绍：**`log4j`的升级版，不能单独使用，推荐是和slf4j配合使用。

​		`logback`分成三个模块： `logback-core`, `logback-classic`, `logback-access`;

​		`logback-core`是基础模块。

3）**Logback的核心对象：** 

​		**Logger:** 日志记录器。

​		**Appender:** 指定日志输出的目的地，目的地可以是控制台或者文件。

​		**Layout:** 日志布局，格式化日志信息的输出。

4）**日志级别：** DEBUG < INFO < WARN < ERROR 。



log4j日志配置转logback: [https://logback.qos.ch/translator/](https://logback.qos.ch/translator/)

日志官网: [https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#boot-features-logging](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#boot-features-logging)



Logback 配置文件推荐使用： [logback_spring.xml](https://github.com/Tianer1123/springboot_test/blob/master/src/main/resources/logback-spring.xml)

注释：

```xml
<configuration> <!--三个子节点-->
	<appender></appender>
  <logger></logger>
  <!--root节点放到最后-->
  <root></root>
</configuration>
```

# Spirngboot整合Elasticsearch

[https://docs.spring.io/spring-data/elasticsearch/docs/3.1.8.RELEASE/reference/html/](https://docs.spring.io/spring-data/elasticsearch/docs/3.1.8.RELEASE/reference/html/)

github有版本匹配说明。



# java8 stream()编程

``` java
 public Object testStream() {
        User u1 = new User(1, "张三", "10010", 23, new Date());
        User u2 = new User(2, "李四", "10011", 24, new Date());
        User u3 = new User(3, "王五", "10012", 25, new Date());
        User u4 = new User(4, "赵六", "10013", 26, new Date());
        User u5 = new User(5, "周七", "10014", 27, new Date());
        User u6 = new User(6, "郑八", "10015", 28, new Date());

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
        userList.add(u6);

        // 获取ids列表
        List<String> ids = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());
        // 获取 id:name hashMap
        Map<Integer, String> map = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
        // key 为 id, value 为 user 对象
        Map<Integer, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        // list中删除id为1的对象
        userList.removeIf(user -> String.valueOf(user.getId()).equals("1"));
        // 根据电话重新排序
        Collections.sort(userList, (o1, o2) -> {
            if (o1.getPhone().compareTo(o2.getPhone()) > 0) {
                return 1;
            } else if (o1.getPhone().compareTo(o2.getPhone()) < 0) {
                return -1;
            } else {
                return 0;
            }
        });
        // userList根据某个字段过滤排序
        Map<String, User> filterMap = userList.stream().collect(Collectors.toMap(user -> "李四".equals(user.getName()) ? user.getPhone() : user.getName(), user -> user));

        return filterMap;
    }
```

# 消息队列RockketMQ、ActiveMQ

## JMS介绍

1. **什么是JMS:** Java消息服务(java message service),Java平台中关于面向消息中间件的接口。

2. jms是一种与厂商无关的API，用来访问消息收发系统消息，它类似JDBC(Java Database Connectivity)。这里，JDBC可以用来访问许多不同关系数据库的API。

3. 使用场景：

   1. 跨平台

   2. 多语言

   3. 多项目

   4. 解耦

   5. 分布式事务

   6. 流量控制

   7. 最终一致性

   8. RPC调用

      上下游对接，数据源变动->通知下属

4. 概念

   1. JMS提供者： Apache ActiveMQ、RabbitMQ、Kafka、Notify、MetaQ、RocketMQ
   2. JMS生产者
   3. JMS消费者
   4. JMS消息
   5. JMS队列
   6. JMS主题

   JMS消息通常有两种类型：点对点类型(point-to-point)，发布/订阅类型(Publish/Subscribe)

5. 编程模型

   MQ中需要使用的一些类

   1. ConnectionFactory: 连接工厂，JMS用它创建连接。
   2. Connection: JMS客户端到JMS Provider的链接。
   3. Session: 一个发送或接受消息的线程。
   4. Destination: 消息目的地，消息发送给谁。
   5. MessageConsumer/MessageProducer: 消息接收者，消费者。

## ActiveMQ5.x

特性:

> - Supports a variety of Cross Language Clients and Protocols from Java, C, C++, C#, Ruby, Perl, Python, PHP
>   - [OpenWire](http://activemq.apache.org/openwire) for high performance clients in Java, C, C++, C#
>   - [Stomp](http://activemq.apache.org/stomp) support so that clients can be written easily in C, Ruby, Perl, Python, PHP, ActionScript/Flash, Smalltalk to talk to ActiveMQ as well as any other popular Message Broker
>   - [AMQP](http://activemq.apache.org/amqp) v1.0 support
>   - [MQTT](http://activemq.apache.org/mqtt) v3.1 support allowing for connections in an IoT environment.
> - full support for the [Enterprise Integration Patterns](http://activemq.apache.org/enterprise-integration-patterns) both in the JMS client and the Message Broker
> - Supports many [advanced features](http://activemq.apache.org/features) such as [Message Groups](http://activemq.apache.org/message-groups), [Virtual Destinations](http://activemq.apache.org/virtual-destinations), [Wildcards](http://activemq.apache.org/wildcards) and [Composite Destinations](http://activemq.apache.org/composite-destinations)
> - Fully supports JMS 1.1 and J2EE 1.4 with support for transient, persistent, transactional and XA messaging
> - [Spring Support](http://activemq.apache.org/spring-support) so that ActiveMQ can be easily embedded into Spring applications and configured using Spring’s XML configuration mechanism
> - Tested inside popular J2EE servers such as TomEE,Geronimo, JBoss, GlassFish and WebLogic
>   - Includes [JCA 1.5 resource adaptors](http://activemq.apache.org/resource-adapter) for inbound & outbound messaging so that ActiveMQ should auto-deploy in any J2EE 1.4 compliant server
> - Supports pluggable [transport protocols](http://activemq.apache.org/uri-protocols) such as [in-VM](http://activemq.apache.org/how-do-i-use-activemq-using-in-jvm-messaging), TCP, SSL, NIO, UDP, multicast, JGroups and JXTA transports
> - Supports very fast [persistence](http://activemq.apache.org/persistence) using JDBC along with a high performance journal
> - Designed for high performance clustering, client-server, peer based communication
> - [REST](http://activemq.apache.org/rest) API to provide technology agnostic and language neutral web based API to messaging
> - [Ajax](http://activemq.apache.org/ajax) to support web streaming support to web browsers using pure DHTML, allowing web browsers to be part of the messaging fabric
> - [CXF and Axis Support](http://activemq.apache.org/axis-and-cxf-support) so that ActiveMQ can be easily dropped into either of these web service stacks to provide reliable messaging
> - Can be used as an in memory JMS provider, ideal for [unit testing JMS](http://activemq.apache.org/how-to-unit-test-jms-code)



[官网地址：http://activemq.apache.org/index.html](http://activemq.apache.org/index.html)



面板:

* Name : 队列名称。
* Number Of Pending Messages: 等待消费的消息个数。
* Number Of Consumers: 当前连接的消费者数目。
* Messages Enqueued: 进入消息队列的总个数，包括出队列的和待消费的，这个数量只增不减。
* Messages Dequeued: 已经消费的消息数量。

### SpringBoot2.x整合ActiveMQ

[官网：https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-activemq](https://docs.spring.io/spring-boot/docs/2.1.4.RELEASE/reference/htmlsingle/#boot-features-activemq)

依赖：

``` xml
<!--整合消息队列-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
<!--如果配置线程池加入如下配置-->
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-pool</artifactId>
</dependency>
```

配置文件：

``` properties
# activeMQ配置
spring.activemq.broker-url=tcp://127.0.0.1:8161
# activeMQ集群配置
# spring.activemq.broker-url=failover:(tcp://localhost:616161,tcp://localhost:717171)
spring.activemq.user=admin
spring.activemq.password=admin
# 线程池的配置,需要添加线程池依赖,不添加开启会报错。
# spring.activemq.pool.enabled=true
# spring.activemq.pool.max-connections=100
```

### 点对点模型

springboot启动类添加：

``` java
@EnableJms // 开启支持jms
```

创建Service：

``` java
@Service
public class PorducerServiceImpl implements ProducerService {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Override
    public void sendMessage(Destination destination, final String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Override
    public void sendMessage(final String message) {
        jmsTemplate.convertAndSend((Destination) this.queue, message);
    }
}
```

被实现的接口：

```java
public interface ProducerService {
    /**
     * 指定消息队列，还有消息
     * @param destination 指定消息队列
     * @param message 消息
     */
    void sendMessage(Destination destination, final String message);

    /**
     * 使用默认消息队列发送消息
     * @param message 消息
     */
    void sendMessage(final String message);
}
```

controller测试

``` java
@RestController
@RequestMapping(value = "/api/v1")
public class OrderController {

    @Autowired
    private ProducerService producerService;

    @GetMapping(value = "order")
    public Object order(String msg) {
        Destination destination = new ActiveMQQueue("order.queue");
        producerService.sendMessage(destination, msg);
        return "Send OK";
    }

    @GetMapping(value = "common")
    public Object common(String msg) {
        producerService.sendMessage(msg);
        return "Send OK";
    }
}
```



*注意，头文件一定引正确。*



消费者实时监听对应的队列：

``` java
@Component
public class OrderConsumer {
    @JmsListener(destination = "order.queue")
    public void receiveQueue(String text) {
        System.out.println("OrderConsumer收到的报文为：" + text);
    }
}
```

### 发布订阅模型

``` properties
# 开启发布订阅配置
spring.jms.pub-sub-domain=true
```

``` java
@Component
public class TopicSub {
    @JmsListener(destination = "video.topic")
    public void receive1(String text) {
        System.out.println("video.topic 消费者: receive1 = " + text);
    }
    @JmsListener(destination = "video.topic")
    public void receive2(String text) {
        System.out.println("video.topic 消费者: receive2 = " + text);
    }
    @JmsListener(destination = "video.topic")
    public void receive3(String text) {
        System.out.println("video.topic 消费者: receive3 = " + text);
    }
}
```

默认不同时支持两种模式（p2p和pub-sub），需要修改一些信息。在启动类中：

``` java
// 需要给topic定义独立的JmsListenerContainer
@Bean
public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
    DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
    bean.setPubSubDomain(true);
    bean.setConnectionFactory(activeMQConnectionFactory);
    return bean;
}
```

并在订阅者中@JmsListener中添加如下代码：

``` java
@JmsListener(destination = "video.topic", containerFactory = "jmsListenerContainerTopic")
```

然后禁用pub-sub配置：

``` properties
# 开启发布订阅配置,不使用配置文件控制
# spring.jms.pub-sub-domain=true
```



## RoekerMQ4.x

阿里开源给Apache的一个高性能消息队列中间件。



# 响应式编程

1. 理解

   * 基于事件驱动(Event-driven)

   * 一系列事件被称为"流"
   * 异步
   * 非阻塞
   * 观察者模式

   例子：

   ``` java
   int a = b + c; // 命令式编程： b和c变化，a不变化
   int a = b + c; // 响应式编程： a的变化与b、c的变化相关(事件驱动)
   ```

2. [https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#boot-features-webflux](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#boot-features-webflux)

