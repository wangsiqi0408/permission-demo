package com.cmmr.permission.controller;

import com.cmmr.permission.common.JsonData;
import com.cmmr.permission.dto.SysAclModuleLevelDto;
import com.cmmr.permission.param.SysAclModuleParam;
import com.cmmr.permission.service.SysAclModuleService;
import com.cmmr.permission.service.SysTreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/aclModule.html")
    public String sysAclModulePage() {
        return "sys/acl";
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData sysAclModuleTree() {
        List<SysAclModuleLevelDto> sysAclModuleDtoList = sysTreeService.sysAclModuleTree();
        return JsonData.success(sysAclModuleDtoList);
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysAclModuleSave(SysAclModuleParam param) {
        sysAclModuleService.save(param);
        return JsonData.success("新增权限模块【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysAclModuleUpdate(SysAclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonData.success("更新权限模块【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysAclModuleDelete(@RequestParam("id") Integer id) {
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
