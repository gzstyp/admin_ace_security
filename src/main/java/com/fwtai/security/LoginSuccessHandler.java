package com.fwtai.security;

import com.fwtai.bean.UserDataDetails;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功操作并返回token
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private ToolToken toolToken;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,final HttpServletResponse response,final Authentication authentication) throws IOException, ServletException{
        //取得账号信息
        final UserDataDetails userDataDetails = (UserDataDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //取token,先去缓存中找,好的解决方案,登录成功后token存储到缓存数据库中,只要token还在过期内，不需要每次重新生成
        final String token = toolToken.generateToken(userDataDetails.getUserId());
        //加载前端菜单
        //final List<SysFrontendMenuTable> menus = service.getMenusByUserName(userDetails.getUsername());
        final Map<String,Object> map = new HashMap<>(3);
        //map.put("username",userDetails.getUsername());
        //map.put("auth",userDetails.getAuthorities());
        //map.put("menus",menus);
        map.put("token",token);
        final String json = ToolClient.queryJson(map);
        ToolClient.responseJson(json,response);
    }
}