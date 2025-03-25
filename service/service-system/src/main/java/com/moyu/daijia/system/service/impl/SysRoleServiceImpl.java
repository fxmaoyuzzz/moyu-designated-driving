package com.moyu.daijia.system.service.impl;

import com.moyu.daijia.model.entity.system.SysRole;
import com.moyu.daijia.model.entity.system.SysUserRole;
import com.moyu.daijia.model.query.system.SysRoleQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.system.AssginRoleVo;
import com.moyu.daijia.system.mapper.SysRoleMapper;
import com.moyu.daijia.system.mapper.SysUserRoleMapper;
import com.moyu.daijia.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PageVo<SysRole> findPage(Page<SysRole> pageParam, SysRoleQuery roleQuery) {
        IPage<SysRole> pageInfo = sysRoleMapper.selectPage(pageParam, roleQuery);
        return new PageVo(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        //查询所有的角色
        List<SysRole> allRolesList = this.list();

        //拥有的角色id
        List<SysUserRole> existUserRoleList = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).select(SysUserRole::getRoleId));
        List<Long> existRoleIdList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole role : allRolesList) {
            //已分配
            if(existRoleIdList.contains(role.getId())) {
                assginRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Transactional
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assginRoleVo.getUserId()));

        for(Long roleId : assginRoleVo.getRoleIdList()) {
            if(null == roleId) continue;
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(assginRoleVo.getUserId());
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }
    }
}
