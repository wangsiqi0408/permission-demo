package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysDept;
import com.cmmr.permission.common.RequestHolder;
import com.cmmr.permission.exception.ParamException;
import com.cmmr.permission.mapper.SysDeptMapper;
import com.cmmr.permission.mapper.SysUserMapper;
import com.cmmr.permission.param.SysDeptParam;
import com.cmmr.permission.service.SysDeptService;
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
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public void save(SysDeptParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getId(), param.getParentId(), param.getName())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder().name(param.getName())
                                           .parentId(param.getParentId())
                                           .seq(param.getSeq())
                                           .remark(param.getRemark()).build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
    }

    @Transactional
    @Override
    public void update(SysDeptParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getId(), param.getParentId(), param.getName())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        SysDept after = SysDept.builder().id(param.getId())
                                         .name(param.getName())
                                         .parentId(param.getParentId())
                                         .seq(param.getSeq())
                                         .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentSysUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
    }

    @Transactional
    protected void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if(!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
            if(CollectionUtils.isNotEmpty(deptList)) {
                for(SysDept dept: deptList) {
                    String level = dept.getLevel();
                    if(level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    public boolean checkExist(Integer deptId, Integer parentId, String deptName) {
        return sysDeptMapper.countByIdOrNameOrParentId(deptId, parentId, deptName) > 0;
    }

    private String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if(sysDept == null){
            return null;
        }
        return sysDept.getLevel();
    }

    @Override
    public void delete(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if(sysDeptMapper.countByParentId(deptId) > 0) {
            throw new ParamException("当前部门下有子部门，无法删除");
        }
        if(sysUserMapper.countByDeptId(deptId) > 0) {
            throw new ParamException("当前部门下有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
