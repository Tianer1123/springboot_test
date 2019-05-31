package com.xdclass.springboot_test.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: tianer
 * @Description: 自定义filter
 * @CreateTime: Created in 14:05 2019/5/14
 */
//@WebFilter(urlPatterns = "/api/*", filterName = "MyFilter")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init MyFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter doFilter");
        HttpServletRequest res = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;
        String userName = res.getParameter("userName");
//        System.out.println("userName: " + userName);
        if ("tianer".equals(userName)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            rep.sendRedirect("/index.html");
        }

    }

    @Override
    public void destroy() {
        System.out.println("destroy MyFilter");
    }
}
