package com.atguigu.crowd.util;

import com.atguigu.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 通用工具类
 */
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

    /**
     * 对明文字符串进行MD5加密
     * @param source 明文字符串
     * @return 加密结果
     */
    public static String md5(String source) {
        if (source == null || source.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 加密
        String algorithm = "md5";
        try {
            // 创建messageDigest对象
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 获取明文字符串对应的字节数组
            byte[] input = source.getBytes();
            // 执行加密
            byte[] output = messageDigest.digest(input);
            // 通过bigInteger转为字符串
            int signum = 1;
            // 创建bigInteger对象
            BigInteger bigInteger = new BigInteger(signum, output);
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
