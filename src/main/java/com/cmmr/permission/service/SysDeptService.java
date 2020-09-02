package com.cmmr.permission.service;

import com.cmmr.permission.param.SysDeptParam;

public interface SysDeptService {

    //新增部门
    void save(SysDeptParam param);

    void update(SysDeptParam param);

    void delete(Integer deptId);

}
