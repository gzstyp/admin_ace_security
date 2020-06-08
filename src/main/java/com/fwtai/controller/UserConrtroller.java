package com.fwtai.controller;

import com.fwtai.service.UserService;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-30 18:34
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@RequestMapping("/user")
public class UserConrtroller{

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(final HttpServletRequest request,final HttpServletResponse response){
        final String json  = userService.register(ToolClient.getFormParams(request));
        ToolClient.responseJson(json,response);
    }
}