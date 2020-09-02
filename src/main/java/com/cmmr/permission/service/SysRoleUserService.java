package com.cmmr.permission.service;

import com.cmmr.permission.bean.SysUser;

import java.util.List;

public interface SysRoleUserService {

    List<SysUser> getListByRoleId(Integer roleId);

    void changeRoleUsers(Integer roleId, List<Integer> userIdList);
}
