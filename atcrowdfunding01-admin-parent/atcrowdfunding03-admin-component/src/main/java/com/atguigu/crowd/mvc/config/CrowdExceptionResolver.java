package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ControllerAdvice 表示当前类是基于注解的异常处理类
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    /**
     * 处理自定义的访问被拒绝异常
     */
    @ExceptionHandler(AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        return commonResolve("admin-login", exception, request, response);
    }

    /**
     * 处理自定义的登陆失败异常
     */
    @ExceptionHandler(LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {
        return commonResolve("admin-login", exception, request, response);
    }

    /**
     * 处理算数异常
     */
    @ExceptionHandler(ArithmeticException.class)
    public ModelAndView resolveArithmeticException(ArithmeticException exception,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {
        return commonResolve("system-error", exception, request, response);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView resolveNullPointException(NullPointerException exception,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException {
        return commonResolve("system-error", exception, request, response);
    }

    /**
     * ExceptionHandler 将一个异常和一个方法关联起来
     * @param viewName 异常处理后要返回的视图页面
     * @param exception 实际捕获的异常类型
     * @param request 当前请求对象
     * @param response 当前响应对象
     * @return json或modelAndView
     * @throws IOException
     */
    private ModelAndView commonResolve(String viewName,
                                       Exception exception,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        // 1.判断请求类型
        boolean judge = CrowdUtil.judgeRequestType(request);
        if (judge) {
            // 2.是Ajax请求，创建resultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // 3.将resultEntity转换为json字符串
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            // 4.做响应
            response.getWriter().write(json);
            // 5.由于上面已经用原生响应对象返回了响应，所以不提供ModelAndView对象
            return null;
        }
        // 6.非Ajax请求，创建modelAndView对象
        ModelAndView modelAndView = new ModelAndView(viewName);
        // 7.将exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 8.返回
        return modelAndView;
    }
}
