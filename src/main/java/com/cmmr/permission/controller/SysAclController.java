package com.cmmr.permission.controller;

import com.cmmr.permission.bean.SysAcl;
import com.cmmr.permission.bean.SysRole;
import com.cmmr.permission.common.JsonData;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.param.SysAclParam;
import com.cmmr.permission.service.SysAclService;
import com.cmmr.permission.service.SysRoleService;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/acl")
public class SysAclController {

    @Resource
    private SysAclService sysAclService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData sysAclPage(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery page) {
        PageResult<SysAcl> result = sysAclService.getSysAclPageByAclModuleId(aclModuleId, page);
        return JsonData.success(result);
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysAclSave(SysAclParam param) {
        sysAclService.save(param);
        return JsonData.success("新增权限点【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysAclUpdate(SysAclParam param) {
        sysAclService.update(param);
        return JsonData.success("更新权限点【" + param.getName() + "】成功！");
    }

    @RequestMapping("/roles.json")
    @ResponseBody
    public JsonData roles(Integer aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getSysRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getSysUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
