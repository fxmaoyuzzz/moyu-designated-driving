package com.moyu.daijia.system.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysRole;
import com.moyu.daijia.model.query.system.SysRoleQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.system.AssginRoleVo;
import com.moyu.daijia.system.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    @Operation(summary = "获取分页列表")
    @PostMapping("findPage/{page}/{limit}")
    public Result<PageVo<SysRole>> findPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,

            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,

            @Parameter(name = "roleQuery", description = "查询对象", required = false)
            @RequestBody SysRoleQuery roleQuery) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        PageVo<SysRole> pageVo = sysRoleService.findPage(pageParam, roleQuery);
        return Result.ok(pageVo);
    }

    @Operation(summary = "获取")
    @GetMapping("getById/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @Operation(summary = "新增角色")
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody @Validated SysRole role) {
        return Result.ok(sysRoleService.save(role));
    }

    @Operation(summary = "修改角色")
    @PutMapping("update")
    public Result<Boolean> update(@RequestBody SysRole role) {
        return Result.ok(sysRoleService.updateById(role));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.ok(sysRoleService.removeById(id));
    }

    @Operation(summary = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result<Boolean> batchRemove(@RequestBody List<Long> idList) {
        sysRoleService.removeByIds(idList);
        return Result.ok();
    }

    @Operation(summary = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result<Map<String, Object>> toAssign(@PathVariable Long userId) {
        Map<String, Object> roleMap = sysRoleService.findRoleByUserId(userId);
        return Result.ok(roleMap);
    }

    @Operation(summary = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result<Boolean> doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok(true);
    }


}

