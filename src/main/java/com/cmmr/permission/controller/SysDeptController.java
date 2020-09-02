package com.cmmr.permission.controller;

import com.cmmr.permission.common.JsonData;
import com.cmmr.permission.dto.SysDeptLevelDto;
import com.cmmr.permission.param.SysDeptParam;
import com.cmmr.permission.service.SysDeptService;
import com.cmmr.permission.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.html")
    public String sysDeptListPage() {
        return "sys/dept";
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData sysDeptTree() {
        List<SysDeptLevelDto> sysDeptLevelDtoList = sysTreeService.sysDeptTree();
        return JsonData.success(sysDeptLevelDtoList);
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysDeptSave(SysDeptParam param) {
        sysDeptService.save(param);
        return JsonData.success("新增部门【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysDeptUpdate(SysDeptParam param) {
        sysDeptService.update(param);
        return JsonData.success("更新部门【" + param.getName() + "】成功！");
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData sysDeptDelete(@RequestParam("id") Integer id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }
}
