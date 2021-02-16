package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleHandler {

    private Logger logger = LoggerFactory.getLogger(RoleHandler.class);

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询
     */
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
}
