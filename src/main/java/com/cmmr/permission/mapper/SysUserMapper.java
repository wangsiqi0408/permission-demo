package com.cmmr.permission.mapper;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.page.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findSysUserByKeyword(@Param("keyword") String keyword);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer sysUserId);

    int countByEmail(@Param("email") String email, @Param("id") Integer sysUserId);

    int countByDeptId(@Param("deptId") int deptId);

    List<SysUser> getSysUserPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery pageQuery);

    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    List<SysUser> getAll();
}