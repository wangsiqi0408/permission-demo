package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysRoleAcl;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.mapper.SysRoleAclMapper;
import com.cmmr.permission.service.SysRoleAclService;
import com.cmmr.permission.utils.IpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        //获取当前角色的原始的权限列表
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if(aclIdList.size() == originAclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if(CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
    }

    @Transactional
    public void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if(CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for(Integer aclId: aclIdList) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId)
                        .aclId(aclId)
                        .operator(RequestHolder.getCurrentSysUser().getUsername())
                        .operateTime(new Date())
                        .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            roleAclList.add(sysRoleAcl);
        }
        sysRoleAclMapper.batchInsert(roleAclList);

    }
}
