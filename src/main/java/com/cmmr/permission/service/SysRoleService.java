package com.cmmr.permission.service;

import com.cmmr.permission.bean.SysRole;
import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.param.SysRoleParam;

import java.util.List;

public interface SysRoleService {

    List<SysRole> getAll();

    void save(SysRoleParam param);

    void update(SysRoleParam param);

    List<SysRole> getSysRoleListByUserId(Integer userId);

    List<SysRole> getSysRoleListByAclId(Integer aclId);

    List<SysUser> getSysUserListByRoleList(List<SysRole> roleList);
}
