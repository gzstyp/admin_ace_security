package com.fwtai.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常类，继承AuthenticationException
 * 在有throws AuthenticationException方法上捕获
 * 方式：throw new  AuthExceptionHandler
*/
public class AuthExceptionHandler  extends AuthenticationException {

    public AuthExceptionHandler(String msg) {
        super(msg);
    }
}