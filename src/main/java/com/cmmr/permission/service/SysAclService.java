package com.cmmr.permission.service;

import com.cmmr.permission.bean.SysAcl;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.param.SysAclParam;

public interface SysAclService {

    void save(SysAclParam sysAclParam);

    void update(SysAclParam sysAclParam);

    PageResult<SysAcl> getSysAclPageByAclModuleId(Integer aclModuleId, PageQuery page);
}
