package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.common.annotation.Log;
import com.moyu.daijia.common.enums.BusinessType;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.mgr.service.SysDeptService;
import com.moyu.daijia.model.entity.system.SysDept;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "部门管理")
@RestController
@RequestMapping(value="/sysDept")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysDeptController {
	
	@Autowired
	private SysDeptService sysDeptService;

	@Operation(summary = "获取")
	@PreAuthorize("hasAuthority('bnt.sysDept.list')")
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable Long id) {
		SysDept sysDept = sysDeptService.getById(id);
		return Result.ok(sysDept);
	}

	@Log(title = "部门管理", businessType = BusinessType.INSERT)
	@Operation(summary = "新增")
	@PreAuthorize("hasAuthority('bnt.sysDept.add')")
	@PostMapping("save")
	public Result save(@RequestBody SysDept sysDept) {
		sysDeptService.save(sysDept);
		return Result.ok();
	}

	@Log(title = "部门管理", businessType = BusinessType.UPDATE)
	@Operation(summary = "修改")
	@PreAuthorize("hasAuthority('bnt.sysDept.update')")
	@PutMapping("update")
	public Result update(@RequestBody SysDept sysDept) {
		sysDeptService.update(sysDept);
		return Result.ok();
	}

	@Log(title = "部门管理", businessType = BusinessType.DELETE)
	@Operation(summary = "删除")
	@PreAuthorize("hasAuthority('bnt.sysDept.remove')")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		sysDeptService.remove(id);
		return Result.ok();
	}

	@Operation(summary = "获取全部部门节点")
	@PreAuthorize("hasAuthority('bnt.sysDept.list')")
	@GetMapping("findNodes")
	public Result findNodes() {
		return Result.ok(sysDeptService.findNodes());
	}

	@Operation(summary = "获取用户部门节点")
	@GetMapping("findUserNodes")
	public Result findUserNodes() {
		return Result.ok(sysDeptService.findUserNodes());
	}

	@Log(title = "部门管理", businessType = BusinessType.STATUS)
	@Operation(summary = "更新状态")
	@GetMapping("updateStatus/{id}/{status}")
	public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
		sysDeptService.updateStatus(id, status);
		return Result.ok();
	}

}

