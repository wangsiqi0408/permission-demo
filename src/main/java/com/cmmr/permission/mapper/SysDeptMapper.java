package com.cmmr.permission.mapper;


import com.cmmr.permission.bean.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllSysDept();

    List<SysDept> getChildDeptListByLevel(String level);

    void batchUpdateLevel(@Param("sysDeptList")List<SysDept> sysDeptList);

    int countByIdOrNameOrParentId(@Param("id")Integer id, @Param("parentId")Integer parentId, @Param("name")String name);

    int countByParentId(@Param("parentId") Integer parentId);
}