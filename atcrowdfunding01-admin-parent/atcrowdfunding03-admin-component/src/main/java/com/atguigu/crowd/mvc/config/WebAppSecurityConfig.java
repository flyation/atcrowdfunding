package com.atguigu.crowd.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 声明配置类
@Configuration
// 启用web下的权限控制
@EnableWebSecurity
// 用Spring Security要继承WebSecurityConfigurerAdapter
// 注意：为了让Spring Security能供针对浏览器的请求进行权限控制，需要让Spring MVC来扫描这个配置类，而不是让Spring来扫描
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
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
                ;
    }
}
