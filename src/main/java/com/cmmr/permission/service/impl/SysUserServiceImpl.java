package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.mapper.SysUserMapper;
import com.cmmr.permission.param.SysUserParam;
import com.cmmr.permission.service.SysUserService;
import com.cmmr.permission.utils.BeanValidator;
import com.cmmr.permission.utils.IpUtil;
import com.cmmr.permission.utils.MD5Util;
import com.cmmr.permission.utils.PasswordUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public void save(SysUserParam param) {
        BeanValidator.check(param);
        if(checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("该邮箱已经被占用");
        }
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("该电话号已经被占用");
        }
        String password = PasswordUtil.randomPassword();

        SysUser sysUser = SysUser.builder().username(param.getUsername())
                                           .password(MD5Util.encrypt(password))
                                           .email(param.getEmail())
                                           .telephone(param.getTelephone())
                                           .deptId(param.getDeptId())
                                           .status(param.getStatus())
                                           .remark(param.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());

        //TODO: sendEmail
        sysUserMapper.insertSelective(sysUser);
    }

    @Transactional
    @Override
    public void update(SysUserParam param) {
        BeanValidator.check(param);
        if(checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("该邮箱已经被占用");
        }
        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("该电话号已经被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        SysUser after = SysUser.builder().id(param.getId())
                .username(param.getUsername())
                .email(param.getEmail())
                .telephone(param.getTelephone())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    public boolean checkEmailExist(String email, Integer sysUserId) {
        return sysUserMapper.countByEmail(email, sysUserId) > 0;
    }

    public boolean checkTelephoneExist(String telephone, Integer sysUserId) {
        return sysUserMapper.countByTelephone(telephone, sysUserId) > 0;
    }

    @Override
    public SysUser findSysUserByKeyword(String keyword) {
        return sysUserMapper.findSysUserByKeyword(keyword);
    }

    @Override
    public PageResult<SysUser> getSysUserPageByDeptId(int deptId, PageQuery page) {
        int count = sysUserMapper.countByDeptId(deptId);
        if(count > 0) {
            List<SysUser> list = sysUserMapper.getSysUserPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
