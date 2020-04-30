package com.fwtai.components;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Passworder extends BCryptPasswordEncoder {

    //生成密码
    @Override
    public String encode(final CharSequence rawPassword) {
        return super.encode(rawPassword);
    }

    //验证匹配密码
    @Override
    public boolean matches(final CharSequence rawPassword,final String encodedPassword) {
        return super.matches(rawPassword,encodedPassword);
    }
}