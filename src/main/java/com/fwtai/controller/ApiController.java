package com.fwtai.controller;

import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试api
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/29 20:42
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/api")
public class ApiController{

    @GetMapping("/list")
    @ResponseBody
    public void list(){
        final String json = ToolClient.createJsonSuccess("任务列表");
        ToolClient.responseJson(json);
    }

    // http://127.0.0.1:804/api/create
    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//角色必须以大写的ROLE_开头(即数据库存的必须是以ROLE_开头)
    public void create(){
        final String json = ToolClient.createJsonSuccess("创建了一个新的任务 for ROLE_ADMIN");
        ToolClient.responseJson(json);
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('ROLE_VIP')")//权限不区分大小写也以一定是以的ROLE_开头
    public void edit(){
        final String json = ToolClient.createJsonSuccess("有ROLE_VIP权限");
        ToolClient.responseJson(json);
    }

    @PostMapping("/register")
    public void register(){
        final String json = ToolClient.createJsonSuccess("不需要任何角色和权限就可以访问");
        ToolClient.responseJson(json);
    }
}