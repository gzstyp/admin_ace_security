package com.fwtai.service;

import com.fwtai.bean.SysUser;
import com.fwtai.components.Passworder;
import com.fwtai.dao.DaoHandle;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 用户账号中心
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-29 18:20
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
@Service
public class UserService{

    @Autowired
    private DaoHandle daoHandle;

    @Autowired
    private Passworder passworder;

    public boolean checkLogin(final String username,final String rawPassword){
        final String password = daoHandle.queryForString("sys_user.login",username);
        if(password == null)return false;
        return passworder.matches(rawPassword,password);
    }

    // 仅仅获取用户userid的角色和(不含权限)
    public List<String> getRoleByUserId(final String userId){
        return daoHandle.queryListEntity("sys_user.getRoleByUserId",userId);
    }

    // 仅仅获取用户userId的角色和权限
    public List<String> getRolePermissions(final String userId){
        return daoHandle.queryListEntity("sys_user.getRolePermissions",userId);
    }

    /**
     * 通过userName查询用户信息,用户登录
     * @param username
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:54
    */
    public SysUser getUserByUserName(final String username){
        return daoHandle.queryForEntity("sys_user.getUserByUserName",username);
    }

    /**
     * 通过userId查询用户的全部角色和权限的信息
     * @param userId
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:53
    */
    public SysUser getUserById(final String userId){
        return daoHandle.queryForEntity("sys_user.getUserById",userId);
    }

    public String register(final HashMap<String,String> params){
        final String p_username = "username";
        final String p_password = "password";
        final String validate = ToolClient.validateField(params,p_username,p_password);
        if(validate != null)
            return validate;
        params.put("kid",ToolString.getIdsChar32());
        params.put("password",passworder.encode(params.get(p_password)));
        final int rows = daoHandle.execute("sys_user.addUser",params);
        daoHandle.execute("sys_user.addPassword",params);
        return ToolClient.executeRows(rows);
    }
}