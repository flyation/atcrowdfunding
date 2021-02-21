package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据账号查询Admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);
        // 2.根据admin的角色和权限
        List<Role> assignedRoleList = roleService.getAssignedRole(admin.getId());
        List<String> assignedAuthNameList = authService.getAssignedAuthNameByAdminId(admin.getId());
        // 3.封装角色和权限到GrantedAuthority集合中
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 3.1遍历assignedRoleList，存入角色到authorities
        for (Role role : assignedRoleList) {
            // 注意，角色需要加前缀”ROLE_“
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            authorities.add(authority);
        }
        // 3.2遍历assignedAuthNameList，存入权限到authorities
        for (String authName : assignedAuthNameList) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authName);
            authorities.add(authority);
        }
        // 4.封装SecurityAdmin对象
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
