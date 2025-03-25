package com.moyu.daijia.system.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysMenu;
import com.moyu.daijia.model.vo.system.AssginMenuVo;
import com.moyu.daijia.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "获取菜单")
    @GetMapping("findNodes")
    public Result<List<SysMenu>> findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @Operation(summary = "新增菜单")
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody SysMenu permission) {
        return Result.ok(sysMenuService.save(permission));
    }

    @Operation(summary = "修改菜单")
    @PutMapping("update")
    public Result<Boolean> update(@RequestBody SysMenu permission) {
        return Result.ok(sysMenuService.updateById(permission));
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.ok(sysMenuService.removeById(id));
    }

    @Operation(summary = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result<List<SysMenu>> toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @Operation(summary = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result<Boolean> doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok(true);
    }
}

