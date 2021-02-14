package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
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

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm.html")
    public String test(ModelMap modelMap) {
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);
        return "target";
    }

    @RequestMapping("/send/array1.html")
    @ResponseBody
    public String array1(@RequestParam("array") List<Integer> array) {
        System.out.println(array);
        return "success";
    }

    @RequestMapping("/send/array2.json")
    @ResponseBody
    public ResultEntity<List<Integer>> array2(@RequestBody List<Integer> array) {
        Logger logger = LoggerFactory.getLogger(TestHandler.class);
        logger.info(array.toString());
        return ResultEntity.successWithData(array);
    }
}
