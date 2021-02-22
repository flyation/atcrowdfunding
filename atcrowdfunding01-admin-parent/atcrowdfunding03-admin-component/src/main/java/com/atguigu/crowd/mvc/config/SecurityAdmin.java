package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 考虑到Spring Security的User对象仅仅包含账号和密码，为了能够获取原始的Admin对象，创建了这个类（继承User，包含Admin）
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    /**
     * 原始的Admin对象
     */
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, Collection<? extends GrantedAuthority> authorities) {
        // 调用父类构造器
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
        // 擦除原始Admin对象的密码，增强安全性
        // （密码验证时使用的是org.springframework.security.core.userdetails.User中的password
        // 已经通过上面的父类构造器传过去了，所以这里可以擦除了，不碍事）
        this.originalAdmin.setUserPswd(null);
    }

    /**
     * 获取原始的Admin对象
     */
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
