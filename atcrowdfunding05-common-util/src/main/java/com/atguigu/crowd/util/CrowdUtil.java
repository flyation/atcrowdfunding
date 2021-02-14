package com.atguigu.crowd.util;

import javax.servlet.http.HttpServletRequest;

public class CrowdUtil {

    /**
     * 判断当前请求是否为Ajax请求
     * @param request 请求对象
     * @return true：是Ajax请求；false：不是Ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        // 1.获取请求头
        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        String hh = request.getHeader("hh");
        // 2.判断
        return (accept != null && accept.contains("application/json"))
                ||
                (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith));
    }
}
