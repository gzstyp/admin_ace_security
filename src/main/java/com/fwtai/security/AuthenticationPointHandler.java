package com.fwtai.security;

import com.fwtai.config.FlagToken;
import com.fwtai.config.RenewalToken;
import com.fwtai.tool.ToolClient;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份校验失败处理器，如 token 错误
 */
@Component
public class AuthenticationPointHandler implements AuthenticationEntryPoint{

    @Override
    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException exception) throws IOException, ServletException{
        final Integer flag = FlagToken.get();
        System.out.println("flag-->"+flag);
        String json = ToolClient.notAuthorized();
        if(flag != null){
            switch (flag){
                case 1:
                    RenewalToken.set("更换token");
                    json = ToolClient.createJsonFail("更换token");
                    break;
                case 2:
                    json = ToolClient.createJsonFail("无效的token");
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
        System.out.println("++++++++++++++"+exception.getMessage()+"++++++++++++++");
        ToolClient.responseJson(json,response);
        // todo 处理 ThreadLocal
    }
}