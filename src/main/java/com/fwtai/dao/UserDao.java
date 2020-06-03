package com.fwtai.dao;

import com.fwtai.bean.SysUser;
import com.fwtai.datasource.DaoHandle;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 用户账号管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class UserDao{

    @Autowired
    private DaoHandle daoHandle;

    public String queryLogin(final String username){
        return daoHandle.queryForString("sys_user.login",username);
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

    @Transactional
    public String addRegister(final HashMap<String,String> params){
        final int rows = daoHandle.execute("sys_user.addUser",params);
        daoHandle.execute("sys_user.addPassword",params);
        return ToolClient.executeRows(rows);
    }
}