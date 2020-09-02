package com.cmmr.permission.filter;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.RequestHolder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        SysUser sysUser = (SysUser)req.getSession().getAttribute("user");

        //未登录
        if(sysUser == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(req);
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
