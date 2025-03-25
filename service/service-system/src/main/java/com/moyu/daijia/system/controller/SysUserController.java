package com.moyu.daijia.system.controller;

import com.moyu.daijia.common.annotation.Log;
import com.moyu.daijia.common.enums.BusinessType;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.util.MD5;
import com.moyu.daijia.model.entity.system.SysUser;
import com.moyu.daijia.model.query.system.SysUserQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "用户管理")
@RestController
@RequestMapping("/sysUser")
@CrossOrigin
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "获取分页列表")
    @PostMapping("findPage/{page}/{limit}")
    public Result<PageVo<SysUser>> findPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,

            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,

            @Parameter(name = "userQuery", description = "查询对象", required = false)
            @RequestBody SysUserQuery sysUserQuery) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        PageVo<SysUser> pageVo = sysUserService.findPage(pageParam, sysUserQuery);
        return Result.ok(pageVo);
    }

    @Operation(summary = "获取用户")
    @GetMapping("getById/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }

    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @Operation(summary = "保存用户")
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody SysUser user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        return Result.ok(sysUserService.save(user));
    }

    @Operation(summary = "更新用户")
    @PutMapping("update")
    public Result<Boolean> updateById(@RequestBody SysUser sysUser) {
        return Result.ok(sysUserService.updateById(sysUser));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.ok(sysUserService.removeById(id));
    }

    @Operation(summary = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok(true);
    }
}

