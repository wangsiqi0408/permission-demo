package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysAcl;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.mapper.SysAclMapper;
import com.cmmr.permission.param.SysAclParam;
import com.cmmr.permission.service.SysAclService;
import com.cmmr.permission.utils.BeanValidator;
import com.cmmr.permission.utils.IpUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysAclServiceImpl implements SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Transactional
    @Override
    public void save(SysAclParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName())
                                     .aclModuleId(param.getAclModuleId())
                                     .url(param.getUrl())
                                     .type(param.getType())
                                     .status(param.getStatus())
                                     .seq(param.getSeq())
                                     .remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        sysAclMapper.insertSelective(acl);
    }

    @Transactional
    @Override
    public void update(SysAclParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId())
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(Integer aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    public String generateCode() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    @Override
    public PageResult<SysAcl> getSysAclPageByAclModuleId(Integer aclModuleId, PageQuery page) {
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if(count > 0 ) {
            List<SysAcl> sysAclList = sysAclMapper.getSysAclPageByAclModuleId(aclModuleId, page);
            return PageResult.<SysAcl>builder().total(count).data(sysAclList).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
