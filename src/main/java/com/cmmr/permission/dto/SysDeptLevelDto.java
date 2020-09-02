package com.cmmr.permission.dto;

import com.cmmr.permission.bean.SysDept;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class SysDeptLevelDto extends SysDept {

    private List<SysDeptLevelDto> deptList = Lists.newArrayList();

    public static SysDeptLevelDto adapt(SysDept sysDept) {
        SysDeptLevelDto dto = new SysDeptLevelDto();
        BeanUtils.copyProperties(sysDept, dto);
        return dto;
    }
}
