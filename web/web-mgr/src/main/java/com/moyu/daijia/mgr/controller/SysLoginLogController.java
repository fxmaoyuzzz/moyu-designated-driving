package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.mgr.service.SysLoginLogService;
import com.moyu.daijia.model.entity.system.SysLoginLog;
import com.moyu.daijia.model.query.system.SysLoginLogQuery;
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
@Tag(name = "系统登录日志管理")
@RestController
@RequestMapping(value="/sysLoginLog")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysLoginLogController {
	
	@Resource
	private SysLoginLogService sysLoginLogService;

	@Operation(summary = "获取分页列表")
	@PostMapping("{page}/{limit}")
	public Result findPage(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysLoginLogVo", description = "查询对象", required = false)
		@RequestBody SysLoginLogQuery sysLoginLogQuery) {
		return Result.ok(sysLoginLogService.findPage(page, limit, sysLoginLogQuery));
	}

	@Operation(summary = "获取")
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable Long id) {
		SysLoginLog sysLoginLog = sysLoginLogService.getById(id);
		return Result.ok(sysLoginLog);
	}

	@Operation(summary = "记录登录日志")
	@PostMapping("recordLoginLog")
	public Result recordLoginLog(@RequestBody SysLoginLog sysLoginLog) {
		sysLoginLogService.recordLoginLog(sysLoginLog);
		return Result.ok();
	}

}

