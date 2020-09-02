package com.cmmr.permission.dto;

import com.cmmr.permission.bean.SysAclModule;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class SysAclModuleLevelDto extends SysAclModule {

    private List<SysAclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<SysAclDto> aclList = Lists.newArrayList();

    public static SysAclModuleLevelDto adapt(SysAclModule sysAclModule) {
        SysAclModuleLevelDto dto = new SysAclModuleLevelDto();
        BeanUtils.copyProperties(sysAclModule, dto);
        return dto;
    }
}
