package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysRoleUser;
import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.mapper.SysRoleUserMapper;
import com.cmmr.permission.mapper.SysUserMapper;
import com.cmmr.permission.service.SysRoleUserService;
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
public class SysRoleUserServiceImpl implements SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getListByRoleId(Integer roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if(CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Override
    public void changeRoleUsers(Integer roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if(userIdList.size() == originUserIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if(CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
    }

    @Transactional
    protected void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if(CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for(Integer userId: userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().userId(userId).roleId(roleId).build();
            roleUser.setOperator(RequestHolder.getCurrentSysUser().getUsername());
            roleUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            roleUser.setOperateTime(new Date());
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }
}
