package com.cmmr.permission.common;

import com.cmmr.permission.bean.SysUser;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {

    private static final ThreadLocal<SysUser> sysUserHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(SysUser sysUser) {
        sysUserHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static SysUser getCurrentSysUser() {
        return sysUserHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    //进程结束后移除
    public static void remove() {
        sysUserHolder.remove();
        requestHolder.remove();
    }
}
