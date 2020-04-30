package com.fwtai.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 要实现UserDetails接口，这个接口是security提供的
*/
public class AuthUser implements UserDetails {

    private String userId;

    private String username;

    private String password;

    private boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(){}

    //不含角色和权限
    public AuthUser(final String userId,final String username,final String password,final Integer enabled){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = (enabled == 0);//0正常;1禁用
    }

    //含有角色和权限
    public AuthUser(final String userId,final String username,final String password,final Integer enabled,final Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = (enabled == 0);//0正常;1禁用
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁
    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
}
