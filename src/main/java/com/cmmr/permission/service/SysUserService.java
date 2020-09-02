package com.cmmr.permission.service;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.param.SysUserParam;

import java.util.List;

public interface SysUserService {

    void save(SysUserParam param);

    void update(SysUserParam param);

    SysUser findSysUserByKeyword(String keyword);

    PageResult<SysUser> getSysUserPageByDeptId(int deptId, PageQuery page);

    List<SysUser> getAll();


}
