package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.common.annotation.Log;
import com.moyu.daijia.common.enums.BusinessType;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.mgr.service.SysRoleService;
import com.moyu.daijia.model.entity.system.SysRole;
import com.moyu.daijia.model.query.system.SysRoleQuery;
import com.moyu.daijia.model.vo.system.AssginRoleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "角色管理")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary = "获取全部角色列表")
    @GetMapping("findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> roleList = sysRoleService.findAll();
        return Result.ok(roleList);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @Operation(summary = "获取分页列表")
    @PostMapping("{page}/{limit}")
    public Result findPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,

            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,

            @Parameter(name = "roleQuery", description = "查询对象", required = false)
            @RequestBody SysRoleQuery roleQuery) {
        return Result.ok(sysRoleService.findPage(page,limit, roleQuery));
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @Operation(summary = "获取")
    @GetMapping("getById/{id}")
    public Result getById(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }

    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @Operation(summary = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody @Validated SysRole role) {
        sysRoleService.save(role);
        return Result.ok();
    }

    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @Operation(summary = "修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role) {
        sysRoleService.update(role);
        return Result.ok();
    }

    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @Operation(summary = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysRoleService.remove(id);
        return Result.ok();
    }

    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @Operation(summary = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        sysRoleService.batchRemove(idList);
        return Result.ok();
    }

    @Operation(summary = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = sysRoleService.toAssign(userId);
        return Result.ok(roleMap);
    }

    @Log(title = "用户管理", businessType = BusinessType.ASSGIN)
    @Operation(summary = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }


}

