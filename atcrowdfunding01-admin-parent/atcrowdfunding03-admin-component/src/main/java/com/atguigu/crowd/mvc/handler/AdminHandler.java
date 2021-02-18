package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    private Logger logger = LoggerFactory.getLogger(AdminHandler.class);

    @Autowired
    private AdminService adminService;

    /**
     * 登录
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session) {
        // 如果能够返回admin则登陆成功，如果账号和密码不正确则抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        // 将登陆成功的admin存入session
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 注销
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 分页查询
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(ModelMap modelMap,
                                    // 使用@RequestParam中的defaultValue指定默认值
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    /**
     * 删除管理员
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword) {
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 新增管理员
     *
     * 注意:此处使用from表单post提交，Spring MVC可直接封装为admin，无需添加@RequestBody注解。
     * 注解@RequestBody是用来将请求体里的json转换为对象的，若请求不设置contentType:'application/json'则报错415
     */
    @RequestMapping("/admin/save.html")
    public String add(Admin admin) {
        adminService.save(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * 跳转管理员编辑页面（数据回显）
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(ModelMap modelMap,
                             @RequestParam("adminId") Integer adminId) {
        // 查询admin
        Admin admin = adminService.getAdminById(adminId);
        // 将admin存入模型
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    /**
     * 更新管理员
     */
    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword) {
        // 执行更新
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}
