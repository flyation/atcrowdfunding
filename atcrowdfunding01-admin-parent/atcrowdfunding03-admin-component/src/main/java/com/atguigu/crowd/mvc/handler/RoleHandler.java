package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleHandler {

    private Logger logger = LoggerFactory.getLogger(RoleHandler.class);

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询
     */
    // 需要有部长角色才能访问（生效的前提：Spring Security配置类中添加了注解@EnableGlobalMethodSecurity(prePostEnabled = true)）
    @PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/info.json")
    @ResponseBody
    public ResultEntity<PageInfo<Role>> getPageInfo(
                              // 使用@RequestParam中的defaultValue指定默认值
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageInfo<Role> pageInfo = roleService.getPageInfo(keyword, pageNum, pageSize);
        return ResultEntity.successWithData(pageInfo);
    }

    /**
     * 保存角色
     */
    @RequestMapping("/role/save.json")
    @ResponseBody
    public ResultEntity saveRole(Role role) {
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 更新角色
     */
    @RequestMapping("/role/update.json")
    @ResponseBody
    public ResultEntity updateRole(Role role) {
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 删除角色
     */
    @RequestMapping("/role/remove/by/role/id/array.json")
    @ResponseBody
    public ResultEntity removeRoleByRoleIdArray(@RequestBody List<Integer> roleIdList) {
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
}
