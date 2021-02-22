package com.atguigu.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 声明配置类
@Configuration
// 启用web下的权限控制
@EnableWebSecurity
// 用Spring Security要继承WebSecurityConfigurerAdapter
// 注意：为了让Spring Security能供针对浏览器的请求进行权限控制，需要让Spring MVC来扫描这个配置类，而不是让Spring来扫描
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 我们自己写的基于数据库认证的UserDetailsService
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 用于密码加密的PasswordEncoder
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 使用基于内存版的测试登录
//        builder
//                .inMemoryAuthentication()       //内存版
//                .withUser("tom")
//                .password("123123")
//                .roles("ADMIN")
//                ;

        // 正式的基于数据库的登录
        builder
                .userDetailsService(userDetailsService)     // 我们自己写的基于数据库认证的UserDetailsService
                .passwordEncoder(passwordEncoder)   // 密码加密
                ;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()    // 请求授权设置
                .antMatchers("/admin/to/login/page.html") // 登录页
                .permitAll()            // 放行
                .antMatchers("/bootstrap/**", "/crowd/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**", "/layer-v3.1.1/**", "/script/**", "/ztree/**") // 静态资源
                .permitAll()            //放行
                .anyRequest()           // 其他请求
                .authenticated()        // 需要认证
                .and()
                .csrf()                 // 防跨站请求登录CSRF
                .disable()              // 禁用CSRF
                .formLogin()            // 表单登录
                .loginPage("/admin/to/login/page.html")         // 登录页面
                .loginProcessingUrl("/security/do/login.html")  // 登录请求
                .defaultSuccessUrl("/admin/to/main/page.html")  // 登陆成功后页面
                .usernameParameter("loginAcct")                 // 账号的请求参数名
                .passwordParameter("userPswd")                  // 密码的请求参数名
                .and()
                .logout()                                       // 开启退出登录
                .logoutUrl("/security/do/logout.html")          // 退出请求地址
                .logoutSuccessUrl("/admin/to/login/page.html")  // 退出成功后前往的页面
                ;
    }
}
