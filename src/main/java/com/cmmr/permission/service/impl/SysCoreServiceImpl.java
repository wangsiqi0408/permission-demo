package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysAcl;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.mapper.SysAclMapper;
import com.cmmr.permission.mapper.SysRoleAclMapper;
import com.cmmr.permission.mapper.SysRoleUserMapper;
import com.cmmr.permission.service.SysCoreService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        Integer userId = RequestHolder.getCurrentSysUser().getId();
        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(Integer roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if(CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    @Override
    public List<SysAcl> getUserAclList(Integer userId) {
        if(isSuperAdmin()) {
            sysAclMapper.getAll();
        }
        List<Integer> userRoleIdList =  sysRoleUserMapper.getRoleIdListByUserId(userId);
        if(CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if(CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getByIdList(userAclIdList);
    }

    public boolean isSuperAdmin() {
        //TODO:
        return true;
    }

    @Override
    public boolean hasUrlAcl(String url) {
        if(isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if(CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclList();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        //规则： 只要有一个权限点有权限，那么我们就认为有权限
        for (SysAcl acl: aclList) {
            //判断一个用户是否有权限点的访问权限
            if(acl == null || acl.getStatus() != 1) { //权限点无效
                continue;
            }
            hasValidAcl = true;
            if(userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if(!hasValidAcl) {
            return true;
        }
        return false;
    }


}
