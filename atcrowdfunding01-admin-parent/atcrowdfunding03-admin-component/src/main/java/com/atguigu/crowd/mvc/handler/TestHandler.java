package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);


    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm.html")
    public String test(ModelMap modelMap, HttpServletRequest request) {
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);
//        int i = 1 / 0;
        boolean judge = CrowdUtil.judgeRequestType(request);
        logger.info("judgeRequestType = " + judge);
        String s = null;
        s.equals("test");
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
    public ResultEntity<List<Integer>> array2(@RequestBody List<Integer> array, HttpServletRequest request) {
        logger.info(array.toString());
        boolean judge = CrowdUtil.judgeRequestType(request);
        logger.info("judgeRequestType = " + judge);
        String s = null;
        s.equals("test");
        return ResultEntity.successWithData(array);
    }
}
