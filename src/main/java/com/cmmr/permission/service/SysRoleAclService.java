package com.cmmr.permission.service;

import java.util.List;

public interface SysRoleAclService {

    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);
}
