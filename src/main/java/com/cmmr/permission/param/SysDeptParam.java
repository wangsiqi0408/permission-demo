package com.cmmr.permission.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SysDeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(min = 2, max = 20, message = "部门名称需要在2到20个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "部门名称展示顺序不能为空")
    private Integer seq;

    @Length(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
}
