package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysAclModule;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.mapper.SysAclMapper;
import com.cmmr.permission.mapper.SysAclModuleMapper;
import com.cmmr.permission.param.SysAclModuleParam;
import com.cmmr.permission.service.SysAclModuleService;
import com.cmmr.permission.utils.BeanValidator;
import com.cmmr.permission.utils.IpUtil;
import com.cmmr.permission.utils.LevelUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;

    @Transactional
    @Override
    public void save(SysAclModuleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getId(), param.getParentId(), param.getName())) {
            throw new ParamException("同一层级下存在相同的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder()
                                             .name(param.getName())
                                             .parentId(param.getParentId())
                                             .status(param.getStatus())
                                             .seq(param.getSeq())
                                             .remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);

    }

    @Transactional
    @Override
    public void update(SysAclModuleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getId(), param.getParentId(), param.getName())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");
        SysAclModule after = SysAclModule.builder().id(param.getId())
                                                   .name(param.getName())
                                                   .parentId(param.getParentId())
                                                   .status(param.getStatus())
                                                   .seq(param.getSeq())
                                                   .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
    }

    @Transactional
    protected void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if(!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix);
            if(CollectionUtils.isNotEmpty(aclModuleList)) {
                for(SysAclModule sysAclModule: aclModuleList) {
                    String level = sysAclModule.getLevel();
                    if(level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        sysAclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKey(after);
    }

    public boolean checkExist(Integer aclModuleId, Integer parentId, String aclModuleName) {
        return sysAclModuleMapper.countByIdOrNameOrParentId(aclModuleId, parentId, aclModuleName) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if(sysAclModule == null){
            return null;
        }
        return sysAclModule.getLevel();
    }

    @Override
    public void delete(Integer id) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在");
        if(sysAclModuleMapper.countByParentId(id) > 0) {
            throw new ParamException("待删除的权限模块下有子模块，不能删除");
        }
        if(sysAclMapper.countByAclModuleId(id) > 0) {
            throw new ParamException("待删除的权限模块下有权限点，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(id);
    }
}
