package com.cmmr.permission.controller;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.JsonData;
import com.cmmr.permission.param.SysRoleParam;
import com.cmmr.permission.service.*;
import com.cmmr.permission.utils.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService sysUserService;


    @RequestMapping("/role.html")
    public String sysRolePage() {
        return "/sys/role";
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData sysRoleList() {
        return JsonData.success(sysRoleService.getAll());
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysRoleSave(SysRoleParam param) {
        sysRoleService.save(param);
        return JsonData.success("新增角色【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysRoleUpdate(SysRoleParam param) {
        sysRoleService.update(param);
        return JsonData.success("更新角色【" + param.getName() + "】成功！");
    }

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(Integer roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    @ResponseBody
    @RequestMapping(value = "/changeAcls.json", method = RequestMethod.POST)
    public JsonData changeAcls(@RequestParam("roleId") Integer roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/userList.json")
    public JsonData userList(Integer roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unSelectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for(SysUser user: allUserList) {
            if(!selectedUserIdSet.contains(user.getId()) && user.getStatus() == 1) {
                unSelectedUserList.add(user);
            }
        }
        //Set<SysUser> collect = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toSet());
        Map<String, Object> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unSelectedUserList);
        return JsonData.success(map);
    }

    @ResponseBody
    @RequestMapping(value = "/changeUsers.json", method = RequestMethod.POST)
    public JsonData changeUsers(@RequestParam("roleId") Integer roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }
}
