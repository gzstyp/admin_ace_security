package com.fwtai.security;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限校验拒绝处理器(拒绝访问)
 */
@Component
public class AccessDeniedService extends JSONAuthentication implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request,HttpServletResponse response,AccessDeniedException accessDeniedException) throws IOException, ServletException{
        //装入token
        final R<String> data = R.failed("权限不足:" + accessDeniedException.getMessage());
        //输出
        this.WriteJSON(request,response,data);
    }
}