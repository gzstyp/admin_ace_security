package com.fwtai.service;

import com.fwtai.bean.SysUser;
import com.fwtai.bean.UserDataDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录处理
*/
@Service
public class UserServiceDetails implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 通过账号查找用户、角色的信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
    */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        final SysUser user = userService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }else {
            return new UserDataDetails(user.getKid(),user.getUserName(),user.getUserPassword(),user.getEnabled());
        }
    }

    public UserDetails getUserById(final String userId){
        final SysUser user = userService.getUserById(userId);
        if(user != null){
            final List<String> roles =  userService.getRolePermissions(userId);
            final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (final String role : roles){
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return new UserDataDetails(user.getKid(),user.getUserName(),user.getUserPassword(),user.getEnabled(),authorities);
        }
        throw new UsernameNotFoundException("账号信息不存在");
    }
}