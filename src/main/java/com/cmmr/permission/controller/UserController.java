package com.cmmr.permission.controller;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.service.SysUserService;
import com.cmmr.permission.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/login.html")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SysUser sysUser = sysUserService.findSysUserByKeyword(username);

        String ret = request.getParameter("ret");
        String errorMsg = "";

        if(StringUtils.isBlank(username)) {
            errorMsg = "用户名不能为空";
            toLoginPage(request, response, errorMsg, username, ret);
        } else if(StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空";
            toLoginPage(request, response, errorMsg, username, ret);
        } else if(sysUser == null) {
            errorMsg = "用户名或密码错误";
            toLoginPage(request, response, errorMsg, username, ret);
        } else if(!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
            toLoginPage(request, response, errorMsg, username, ret);
        } else if(sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
            toLoginPage(request, response, errorMsg, username, ret);
        } else {
            //login success
            request.getSession().setAttribute("user", sysUser);
            if(StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            }else {
                response.sendRedirect("/admin/index.html");
            }
        }
    }

    @RequestMapping("/logout.html")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/admin/logout.html");
    }

    private void toLoginPage(HttpServletRequest request, HttpServletResponse response,
                             String errorMsg, String username, String ret) throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if(StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
