package com.cmmr.permission.service;

import com.cmmr.permission.dto.SysAclModuleLevelDto;
import com.cmmr.permission.dto.SysDeptLevelDto;

import java.util.List;

public interface SysTreeService {

    List<SysDeptLevelDto> sysDeptTree();

    List<SysAclModuleLevelDto> sysAclModuleTree();

    List<SysAclModuleLevelDto> roleTree(Integer roleId);

    List<SysAclModuleLevelDto> userAclTree(Integer userId);
}
