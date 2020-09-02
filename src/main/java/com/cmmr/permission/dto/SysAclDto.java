package com.cmmr.permission.dto;

import com.cmmr.permission.bean.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ToString
public class SysAclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static SysAclDto adapt(SysAcl sysAcl) {
        SysAclDto dto = new SysAclDto();
        BeanUtils.copyProperties(sysAcl, dto);
        return dto;
    }

}
