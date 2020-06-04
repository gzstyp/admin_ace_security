package com.fwtai.security;

import com.fwtai.components.Passworder;
import com.fwtai.config.ConfigFile;
import com.fwtai.service.UserServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * Security授权配置主文件
*/
@EnableWebSecurity//该注解获得了Spring Security和MVC集成支持
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    //若出错则用上面的方式注解,这个也是可以的
    @Autowired
    private UserServiceDetails userDetailsService;

    @Autowired
    private RequestFilter requestFilter;

    @Autowired
    private AuthenticationPointHandler authenticationPointHandler;

    @Autowired
    private AccessDeniedService accessDeniedService;

    //登录成功处理器
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    //退出处理器
    @Autowired
    private LogoutService logoutService;

    @Autowired
    private LogoutSuccessService logoutSuccessService;

    @Autowired
    private Passworder passworder;

    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     * @param builder
     * @throws Exception
    */
    @Autowired
    public void configureAuthentication(final AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passworder);
    }

    //处理静态资源或忽略的接口
    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring().antMatchers(ConfigFile.IGNORE_URLS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        //第1步：解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求|探测）
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        //第2步：让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        //http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().headers().cacheControl();
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //第3步：请求权限配置,放行注册API请求，其它任何请求都必须经过身份验证.
        http.authorizeRequests().antMatchers(HttpMethod.POST,ConfigFile.URL_REGISTER).permitAll()//注意还有请求方式
        .anyRequest().authenticated();//不走动态加载权限的处理

        //第4步：拦截账号、密码。覆盖 UsernamePasswordAuthenticationFilter过滤器
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);

        //第5步：拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        //第6步：处理异常情况：认证失败和权限不足
        // todo,要是没有这个则跳转到登录界面
        http.exceptionHandling().authenticationEntryPoint(authenticationPointHandler).accessDeniedHandler(accessDeniedService);

        //第7步：登录(如果报错则使用下面的那个)
        http.formLogin()
            .failureForwardUrl(ConfigFile.URL_LOGIN_PAGE)
            .loginPage(ConfigFile.URL_LOGIN_PAGE)
            .loginProcessingUrl(ConfigFile.URL_PROCESSING)
            .usernameParameter("username")
            .passwordParameter("password")
            .permitAll();

        //第7步：登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
        //http.formLogin();

        //第8步：退出
        http.logout().addLogoutHandler(logoutService).logoutSuccessHandler(logoutSuccessService);
    }

    /**
     * 手动注册账号、密码拦截器(感觉这个不管用???)
     * @return
     * @throws Exception
     */
    @Bean
    public LoginAuthFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        final LoginAuthFilter filter = new LoginAuthFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}