package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.mgr.service.SysOperLogService;
import com.moyu.daijia.model.entity.system.SysOperLog;
import com.moyu.daijia.model.query.system.SysOperLogQuery;
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
	@PostMapping("{page}/{limit}")
	public Result findPage(
		@Parameter(name = "page", description = "当前页码", required = true)
		@PathVariable Long page,
	
		@Parameter(name = "limit", description = "每页记录数", required = true)
		@PathVariable Long limit,
	
		@Parameter(name = "sysOperLogVo", description = "查询对象", required = false)
		@RequestBody SysOperLogQuery sysOperLogQuery) {
		return Result.ok(sysOperLogService.findPage(page, limit, sysOperLogQuery));
	}

	@Operation(summary = "获取")
	@GetMapping("getById/{id}")
	public Result getById(@PathVariable Long id) {
		SysOperLog sysOperLog = sysOperLogService.getById(id);
		return Result.ok(sysOperLog);
	}

	@Operation(summary = "记录日志")
	@PostMapping("saveSysLog")
	public Result saveSysLog(@RequestBody SysOperLog sysOperLog) {
		sysOperLog.setOperName(AuthContextHolder.getUserId()+"");
		sysOperLogService.saveSysLog(sysOperLog);
		return Result.ok();
	}

}

