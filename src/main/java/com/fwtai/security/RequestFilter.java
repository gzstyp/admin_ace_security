package com.fwtai.security;

import com.fwtai.config.ConfigFile;
import com.fwtai.config.FlagToken;
import com.fwtai.service.UserServiceDetails;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token拦截器(最先请求的拦截器)||更换token在这里实现
 */
@Component
public class RequestFilter extends OncePerRequestFilter {

    @Resource
    private UserServiceDetails userDetailsService;

    @Autowired
    private ToolJWT toolToken;

    private String header = "Authorization";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain) throws ServletException, IOException {
        final String uri = request.getRequestURI();
        final String[] urls = ConfigFile.IGNORE_URLS;
        for(int x = 0; x < urls.length; x++){
            final String url = urls[x];
            if(uri.equals(url)){
                chain.doFilter(request, response);
                return;
            }
        }
        final String token = request.getHeader(header);
        if (!StringUtils.isEmpty(token)){
            //判断令牌是否过期，默认是一周
            //比较好的解决方案是：
            //登录成功获得token后，将token存储到数据库（redis）
            //将数据库版本的token设置过期时间为15~30分钟
            //如果数据库中的token版本过期，重新刷新获取新的token
            //注意：刷新获得新token是在token过期时间内有效。
            //如果token本身的过期（1周），强制登录，生成新token。
            try {
                toolToken.parser(token);
                //通过令牌获取用户名称
                final String userId = toolToken.extractUserId(token);
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                final SecurityContext context = SecurityContextHolder.getContext();
                if (userId != null && context.getAuthentication() == null) {
                    //通过用户信息得到UserDetails
                    final UserDetails userDetails = userDetailsService.getUserById(userId);
                    //验证令牌有效性
                    final boolean validata = toolToken.validateToken(token,userId);;
                    if (validata){
                        // 将用户信息存入 authentication，方便后续校验,这个方法是要保存角色权限信息的
                        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                        context.setAuthentication(authentication);
                    }
                }
            } catch (final Exception e) {
                System.out.println(e.getClass());
                if(e instanceof ExpiredJwtException){
                    System.out.println("该更换token了呢");
                    //标记为 该更换token了呢
                    FlagToken.set(1);
                    //chain.doFilter(request,response);
                }else if(e instanceof JwtException){
                    //标记为 token 无效
                    FlagToken.set(2);
                    System.out.println("无效的token");
                }
            }
            //chain.doFilter(request, response);
        }
        chain.doFilter(request,response);
    }
}