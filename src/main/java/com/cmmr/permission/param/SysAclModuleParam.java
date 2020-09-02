package com.cmmr.permission.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SysAclModuleParam {

    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2, max = 20, message = "权限模块名称需要在2到20个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Min(value = 0, message = "权限模块状态不合法")
    @Max(value = 2, message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
}
