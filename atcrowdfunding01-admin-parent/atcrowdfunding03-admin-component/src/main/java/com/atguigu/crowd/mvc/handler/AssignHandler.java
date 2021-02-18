package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    private Logger logger = LoggerFactory.getLogger(AssignHandler.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

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

    /**
     * 跳转角色分配页面（数据回显）
     */
    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(@RequestParam("adminId") Integer adminId,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyword") String keyword,
                                            // 允许取消某个管理员的所有权限，所以roleIdList可以没有值
                                            @RequestParam(value = "roleIdList", required = false) List<String> roleIdList) {
        roleService.saveAdminRoleRelationship(adminId, roleIdList);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 查询权限树数据
     */
    @RequestMapping("/assign/get/all/auth.json")
    @ResponseBody
    public ResultEntity<List<Auth>> getAllAuth() {
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }

    /**
     * 根据roleId查询authId
     */
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    @ResponseBody
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId") Integer roleId) {
        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    /**
     * 更新角色role绑定的权限auth
     */
    @RequestMapping("/assign/do/role/assign/auth.json")
    @ResponseBody
    public ResultEntity saveRoleAuthRelationship(@RequestBody Map<String, List<Integer>> map) {
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
