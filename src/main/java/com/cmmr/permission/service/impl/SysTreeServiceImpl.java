package com.cmmr.permission.service.impl;

import com.cmmr.permission.bean.SysAcl;
import com.cmmr.permission.bean.SysAclModule;
import com.cmmr.permission.bean.SysDept;
import com.cmmr.permission.dto.SysAclDto;
import com.cmmr.permission.dto.SysAclModuleLevelDto;
import com.cmmr.permission.dto.SysDeptLevelDto;
import com.cmmr.permission.mapper.SysAclMapper;
import com.cmmr.permission.mapper.SysAclModuleMapper;
import com.cmmr.permission.mapper.SysDeptMapper;
import com.cmmr.permission.service.SysCoreService;
import com.cmmr.permission.service.SysTreeService;
import com.cmmr.permission.utils.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 树结构service
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public List<SysAclModuleLevelDto> userAclTree(Integer userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<SysAclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            SysAclDto dto = SysAclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    //部门树
    @Override
    public List<SysDeptLevelDto> sysDeptTree() {
        List<SysDept> sysDeptList = sysDeptMapper.getAllSysDept();
        List<SysDeptLevelDto> dtoList = Lists.newArrayList();

        for(SysDept dept: sysDeptList) {
            SysDeptLevelDto dto = SysDeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return sysDeptListToTree(dtoList);
    }

    public List<SysDeptLevelDto> sysDeptListToTree(List<SysDeptLevelDto> sysDeptLevelList) {
        if(CollectionUtils.isEmpty(sysDeptLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1, dept2, dept3 ...]
        Multimap<String, SysDeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<SysDeptLevelDto> rootList = Lists.newArrayList();
        for(SysDeptLevelDto dto: sysDeptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if(LevelUtil.ROOT.endsWith(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        //按照seq从小到大排序
        Collections.sort(rootList, sysDeptSeqComparator);

        //递归生成树
        transformSysDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    public void transformSysDeptTree(List<SysDeptLevelDto> sysDeptLevelList, String level, Multimap<String, SysDeptLevelDto> levelDeptMap) {
        for(int i=0; i<sysDeptLevelList.size(); i++) {
            //遍历该层的每个元素
            SysDeptLevelDto sysDeptLevelDto = sysDeptLevelList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, sysDeptLevelDto.getId());
            //处理下一级
            List<SysDeptLevelDto> tempSysDeptList = (List<SysDeptLevelDto>)levelDeptMap.get(nextLevel);
            if(CollectionUtils.isNotEmpty(tempSysDeptList)) {
                //排序
                Collections.sort(tempSysDeptList, sysDeptSeqComparator);
                //设置下一层部门
                sysDeptLevelDto.setDeptList(tempSysDeptList);
                //进入到下一层处理
                transformSysDeptTree(tempSysDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    public Comparator<SysDeptLevelDto> sysDeptSeqComparator = new Comparator<SysDeptLevelDto>() {
        @Override
        public int compare(SysDeptLevelDto o1, SysDeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    //权限模块树
    @Override
    public List<SysAclModuleLevelDto> sysAclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllSysAclModule();
        List<SysAclModuleLevelDto> dtoList = Lists.newArrayList();
        for(SysAclModule aclModule: aclModuleList) {
            SysAclModuleLevelDto sysAclModuleDto = SysAclModuleLevelDto.adapt(aclModule);
            dtoList.add(sysAclModuleDto);
        }
        return sysAclModuleListToTree(dtoList);
    }

    public List<SysAclModuleLevelDto> sysAclModuleListToTree(List<SysAclModuleLevelDto> aclModuleLevelDtoList) {
        if(CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }
        //level -> [aclModule1, aclModule2, aclModule3, ...]
        Multimap<String, SysAclModuleLevelDto> aclModuleLevelMap = ArrayListMultimap.create();
        List<SysAclModuleLevelDto> rootList = Lists.newArrayList();
        for(SysAclModuleLevelDto dto: aclModuleLevelDtoList) {
            aclModuleLevelMap.put(dto.getLevel(), dto);
            if(LevelUtil.ROOT.endsWith(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        //按照seq从小到大排序
        Collections.sort(rootList, sysAclModuleSeqComparator);

        //递归生成树
        transformSysAclModuleTree(rootList, LevelUtil.ROOT, aclModuleLevelMap);
        return rootList;
    }

    public void transformSysAclModuleTree(List<SysAclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String, SysAclModuleLevelDto> aclModuleLevelMap) {
        for(int i=0; i<aclModuleLevelDtoList.size(); i++) {
            //遍历该层的每个元素
            SysAclModuleLevelDto sysAclModuleLevelDto = aclModuleLevelDtoList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, sysAclModuleLevelDto.getId());
            //处理下一级
            List<SysAclModuleLevelDto> tempSysAclModuleList = (List<SysAclModuleLevelDto>)aclModuleLevelMap.get(nextLevel);
            if(CollectionUtils.isNotEmpty(tempSysAclModuleList)) {
                //排序
                Collections.sort(tempSysAclModuleList, sysAclModuleSeqComparator);
                //设置下一层
                sysAclModuleLevelDto.setAclModuleList(tempSysAclModuleList);
                //进入到下一层处理
                transformSysAclModuleTree(tempSysAclModuleList, nextLevel, aclModuleLevelMap);
            }
        }
    }

    public Comparator<SysAclModuleLevelDto> sysAclModuleSeqComparator = new Comparator<SysAclModuleLevelDto>() {
        @Override
        public int compare(SysAclModuleLevelDto o1, SysAclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    @Override
    public List<SysAclModuleLevelDto> roleTree(Integer roleId) {
        //1.当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        //2.当前角色已分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        //3.当前系统所有权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        List<SysAclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());


        for(SysAcl sysAcl: allAclList) {
            SysAclDto dto = SysAclDto.adapt(sysAcl);
            if(userAclIdSet.contains(dto.getId())) {
                dto.setHasAcl(true);
            }
            if(roleAclIdSet.contains(dto.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<SysAclModuleLevelDto> aclListToTree(List<SysAclDto> aclDtoList) {
        if(CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<SysAclModuleLevelDto> aclModuleLevelList = sysAclModuleTree();
        Multimap<Integer, SysAclDto> moduleIdAclMap = ArrayListMultimap.create();
        for(SysAclDto aclDto: aclDtoList) {
            if(aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclListWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    public void bindAclListWithOrder(List<SysAclModuleLevelDto> aclModuleLevelList, Multimap<Integer, SysAclDto> moduleIdAclMap) {
        if(CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for(SysAclModuleLevelDto dto: aclModuleLevelList) {
            List<SysAclDto> aclDtoList = (List<SysAclDto>)moduleIdAclMap.get(dto.getId());
            if(CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, sysAclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclListWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }

    public Comparator<SysAclDto> sysAclSeqComparator = ((o1, o2) -> o1.getSeq() - o2.getSeq());
}
