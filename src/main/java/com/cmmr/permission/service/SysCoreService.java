package com.cmmr.permission.service;

import com.cmmr.permission.bean.SysAcl;

import java.util.List;

public interface SysCoreService {

    List<SysAcl> getCurrentUserAclList();

    List<SysAcl> getRoleAclList(Integer roleId);

    List<SysAcl> getUserAclList(Integer userId);

    boolean hasUrlAcl(String servletPath);

}
