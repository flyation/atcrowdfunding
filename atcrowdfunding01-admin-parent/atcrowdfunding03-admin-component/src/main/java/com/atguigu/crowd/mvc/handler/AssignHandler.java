package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssignHandler {

    private Logger logger = LoggerFactory.getLogger(AssignHandler.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    /**
     * 跳转角色分配页面（数据回显）
     */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toEditPage(ModelMap modelMap,
                             @RequestParam("adminId") Integer adminId) {
        // 1.查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 2.查询未分配的角色
        List<Role> unassignedRoleList = roleService.getUnassignedRole(adminId);
        // 3.存入模型（本质上是request.setAttribute("attrName", attrValue)）
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unassignedRoleList", unassignedRoleList);
        return "assign-role";
    }
}
