package com.cmmr.permission.controller;

import com.cmmr.permission.bean.SysUser;
import com.cmmr.permission.common.JsonData;
import com.cmmr.permission.common.page.PageQuery;
import com.cmmr.permission.common.page.PageResult;
import com.cmmr.permission.param.SysUserParam;
import com.cmmr.permission.service.SysRoleService;
import com.cmmr.permission.service.SysTreeService;
import com.cmmr.permission.service.SysUserService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysTreeService sysTreeService;
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/noAuth.html")
    public String noAuth() {
        return "/sys/noAuth";
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysUserSave(SysUserParam param) {
        sysUserService.save(param);
        return JsonData.success("新增用户【" + param.getUsername() + "】成功！");
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysUserUpdate(SysUserParam param) {
        sysUserService.update(param);
        return JsonData.success("更新用户【" + param.getUsername() + "】成功！");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData sysUserPage(@RequestParam("deptId") Integer deptId, PageQuery page) {
        PageResult<SysUser> result = sysUserService.getSysUserPageByDeptId(deptId, page);
        return JsonData.success(result);
    }

    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(Integer userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getSysRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
