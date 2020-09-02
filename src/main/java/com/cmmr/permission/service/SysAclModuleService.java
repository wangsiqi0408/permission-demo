package com.cmmr.permission.service;

import com.cmmr.permission.param.SysAclModuleParam;

public interface SysAclModuleService {

    void save(SysAclModuleParam param);

    void update(SysAclModuleParam param);

    void delete(Integer id);
}
