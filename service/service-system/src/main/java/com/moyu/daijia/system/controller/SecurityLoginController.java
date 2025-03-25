package com.moyu.daijia.system.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysUser;
import com.moyu.daijia.model.vo.system.LoginVo;
import com.moyu.daijia.system.service.SysMenuService;
import com.moyu.daijia.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "security登录管理")
@RestController
@RequestMapping(value="/securityLogin")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SecurityLoginController {
	
	@Resource
	private SysUserService sysUserService;

	@Resource
	private SysMenuService sysMenuService;

	@Operation(summary = "模拟登录")
	@PostMapping("login")
	public Result login(@RequestBody LoginVo loginVo) {
		log.info(JSON.toJSONString(loginVo));
		return Result.ok();
	}

	@Operation(summary = "根据用户名获取用户信息")
	@GetMapping("getByUsername/{username}")
	public Result<SysUser> getByUsername(@PathVariable String username) {
		return Result.ok(sysUserService.getByUsername(username));
	}

	@Operation(summary = "获取用户按钮权限")
	@GetMapping("findUserPermsList/{userId}")
	public Result<List<String>> findUserPermsList(@PathVariable Long userId) {
		return Result.ok(sysMenuService.findUserPermsList(userId));
	}

	@Operation(summary = "获取用户信息")
	@GetMapping("getUserInfo/{userId}")
	public Result<Map<String, Object>> getUserInfo(@PathVariable Long userId) {
		Map<String, Object> map = sysUserService.getUserInfo(userId);
		return Result.ok(map);
	}
}

