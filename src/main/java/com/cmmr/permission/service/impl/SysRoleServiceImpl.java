package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysRole;
import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.mapper.SysRoleAclMapper;
import com.cmmr.permission.mapper.SysRoleMapper;
import com.cmmr.permission.mapper.SysRoleUserMapper;
import com.cmmr.permission.mapper.SysUserMapper;
import com.cmmr.permission.param.SysRoleParam;
import com.cmmr.permission.service.SysRoleService;
import com.cmmr.permission.utils.BeanValidator;
import com.cmmr.permission.utils.IpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysUserMapper sysUserMapper;


    @Transactional
    @Override
    public void save(SysRoleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getName(), param.getId())) {
            throw new ParamException("该角色名称已经存在");
        }

        SysRole sysRole = SysRole.builder().name(param.getName())
                                           .type(param.getType())
                                           .status(param.getStatus())
                                           .remark(param.getRemark()).build();
        sysRole.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());
        sysRoleMapper.insertSelective(sysRole);
    }

    @Transactional
    @Override
    public void update(SysRoleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getName(), param.getId())) {
            throw new ParamException("该角色名称已经存在");
        }

        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder().id(param.getId())
                .name(param.getName())
                .type(param.getType())
                .status(param.getStatus())
                .remark(param.getRemark()).build();

        after.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public List<SysRole> getSysRoleListByUserId(Integer userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if(CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysRole> getSysRoleListByAclId(Integer aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if(CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysUser> getSysUserListByRoleList(List<SysRole> roleList) {
        if(CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if(CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Override
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    public boolean checkExist(String roleName, Integer id) {
        return sysRoleMapper.countByName(roleName, id) > 0;
    }
}
