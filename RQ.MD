# 使用SpringBoot遇到的问题

## 1. springboot2.x整合thymeleaf的时候，页面弹不出来？

需要了解两个注解：

- `@Controller:` `retrun`返回的是视图页面。
- `@RestController`: `return`返回的是对象。相当于 `@Controller` + `@ResponseBody`。



