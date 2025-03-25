package com.moyu.daijia.system.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysDept;
import com.moyu.daijia.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping(value="/sysDept")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysDeptController {
	
	@Resource
	private SysDeptService sysDeptService;

	@Operation(summary = "获取")
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable Long id) {
		SysDept sysDept = sysDeptService.getById(id);
		return Result.ok(sysDept);
	}

	@Operation(summary = "新增")
	@PostMapping("save")
	public Result<Boolean> save(@RequestBody SysDept sysDept) {
		return Result.ok(sysDeptService.save(sysDept));
	}

	@Operation(summary = "修改")
	@PutMapping("update")
	public Result<Boolean> update(@RequestBody SysDept sysDept) {
		return Result.ok(sysDeptService.updateById(sysDept));
	}

	@Operation(summary = "删除")
	@DeleteMapping("remove/{id}")
	public Result<Boolean> remove(@PathVariable Long id) {
		return Result.ok(sysDeptService.removeById(id));
	}

	@Operation(summary = "获取全部部门节点")
	@GetMapping("findNodes")
	public Result<List<SysDept>> findNodes() {
		return Result.ok(sysDeptService.findNodes());
	}

	@Operation(summary = "获取用户部门节点")
	@GetMapping("findUserNodes")
	public Result<List<SysDept>> findUserNodes() {
		return Result.ok(sysDeptService.findUserNodes());
	}

	@Operation(summary = "更新状态")
	@GetMapping("updateStatus/{id}/{status}")
	public Result<Boolean> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
		sysDeptService.updateStatus(id, status);
		return Result.ok(true);
	}

}

