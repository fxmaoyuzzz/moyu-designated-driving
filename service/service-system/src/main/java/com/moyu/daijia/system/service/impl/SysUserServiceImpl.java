package com.moyu.daijia.system.service.impl;

import com.moyu.daijia.model.entity.system.SysUser;
import com.moyu.daijia.model.query.system.SysUserQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.system.RouterVo;
import com.moyu.daijia.system.mapper.SysUserMapper;
import com.moyu.daijia.system.service.SysMenuService;
import com.moyu.daijia.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysMenuService sysMenuService;

	@Override
	public PageVo<SysUser> findPage(Page<SysUser> pageParam, SysUserQuery sysUserQuery) {
		IPage<SysUser> pageInfo = sysUserMapper.selectPage(pageParam, sysUserQuery);
		return new PageVo(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
	}

	@Transactional
	@Override
	public void updateStatus(Long id, Integer status) {
		SysUser sysUser = this.getById(id);
		if(status.intValue() == 1) {
			sysUser.setStatus(status);
		} else {
			sysUser.setStatus(0);
		}
		this.updateById(sysUser);
	}

	@Override
	public SysUser getByUsername(String username) {
		return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
	}

	@Override
	public Map<String, Object> getUserInfo(Long userId) {
		Map<String, Object> result = new HashMap<>();
		SysUser sysUser = this.getById(userId);

		//根据用户id获取菜单权限值
		List<RouterVo> routerVoList = sysMenuService.findUserMenuList(sysUser.getId());
		//根据用户id获取用户按钮权限
		List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

		result.put("name", sysUser.getName());
		result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
		//当前权限控制使用不到，我们暂时忽略
		result.put("roles",  new HashSet<>());
		result.put("buttons", permsList);
		result.put("routers", routerVoList);
		return result;
	}
}
