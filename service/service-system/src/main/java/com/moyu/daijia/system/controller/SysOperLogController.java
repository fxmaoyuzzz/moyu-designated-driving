package com.moyu.daijia.system.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysOperLog;
import com.moyu.daijia.model.query.system.SysOperLogQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.system.service.SysOperLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author qy
 *
 */
@Tag(name = "系统操作日志管理")
@RestController
@RequestMapping(value="/sysOperLog")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysOperLogController {
	
	@Resource
	private SysOperLogService sysOperLogService;

	@Operation(summary = "获取分页列表")
	@PostMapping("findPage/{page}/{limit}")
	public Result findPage(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysOperLogVo", description = "查询对象", required = false)
		@RequestBody SysOperLogQuery sysOperLogQuery) {
		Page<SysOperLog> pageParam = new Page<>(page, limit);
		PageVo<SysOperLog> pageVo = sysOperLogService.findPage(pageParam, sysOperLogQuery);
		return Result.ok(pageVo);
	}

	@Operation(summary = "获取")
	@GetMapping("getById/{id}")
	public Result<SysOperLog> getById(@PathVariable Long id) {
		SysOperLog sysOperLog = sysOperLogService.getById(id);
		return Result.ok(sysOperLog);
	}

	@Operation(summary = "记录日志")
	@PostMapping("saveSysLog")
	public Result<Boolean> saveSysLog(@RequestBody SysOperLog sysOperLog) {
		sysOperLogService.saveSysLog(sysOperLog);
		return Result.ok(true);
	}

}

