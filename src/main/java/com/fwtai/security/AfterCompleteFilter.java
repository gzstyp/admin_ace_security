package com.fwtai.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-30 14:27
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Component
public class AfterCompleteFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(final HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException{
        System.out.println("最后执行吗?");
        filterChain.doFilter(request, response);
    }
}