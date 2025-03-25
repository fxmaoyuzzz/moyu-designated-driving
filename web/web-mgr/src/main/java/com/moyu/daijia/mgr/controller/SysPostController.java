package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.common.annotation.Log;
import com.moyu.daijia.common.enums.BusinessType;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.mgr.service.SysPostService;
import com.moyu.daijia.model.entity.system.SysPost;
import com.moyu.daijia.model.query.system.SysPostQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "岗位管理")
@RestController
@RequestMapping(value="/sysPost")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysPostController {
	
	@Resource
	private SysPostService sysPostService;

	@Operation(summary = "获取分页列表")
    @PreAuthorize("hasAuthority('bnt.sysPost.list')")
	@PostMapping("{page}/{limit}")
	public Result findPage(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysPostVo", description = "查询对象", required = false)
		@RequestBody SysPostQuery sysPostQuery) {
		return Result.ok(sysPostService.findPage(page, limit, sysPostQuery));
	}

	@Operation(summary = "获取")
    @PreAuthorize("hasAuthority('bnt.sysPost.list')")
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable Long id) {
		SysPost sysPost = sysPostService.getById(id);
		return Result.ok(sysPost);
	}

	@GetMapping("findAll")
	public Result findAll() {
		return Result.ok(sysPostService.findAll());
	}

	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@Operation(summary = "新增")
    @PreAuthorize("hasAuthority('bnt.sysPost.add')")
	@PostMapping("save")
	public Result save(@RequestBody SysPost sysPost) {
		sysPostService.save(sysPost);
		return Result.ok();
	}

	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('bnt.sysPost.update')")
	@PutMapping("update")
	public Result update(@RequestBody SysPost sysPost) {
		sysPostService.update(sysPost);
		return Result.ok();
	}

	@Log(title = "岗位管理", businessType = BusinessType.DELETE)
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('bnt.sysPost.remove')")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		sysPostService.remove(id);
		return Result.ok();
	}

	@Operation(summary = "更新状态")
	@GetMapping("updateStatus/{id}/{status}")
	public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
		sysPostService.updateStatus(id, status);
		return Result.ok();
	}
	
}

