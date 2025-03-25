package com.moyu.daijia.mgr.service.impl;

import com.moyu.daijia.mgr.service.SysMenuService;
import com.moyu.daijia.model.entity.system.SysMenu;
import com.moyu.daijia.model.vo.system.AssginMenuVo;
import com.moyu.daijia.system.client.SysMenuFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuFeignClient sysMenuFeignClient;

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuFeignClient.save(sysMenu);
    }

    @Override
    public void update(SysMenu sysMenu) {
        sysMenuFeignClient.update(sysMenu);
    }

    @Override
    public void remove(Long id) {
        sysMenuFeignClient.remove(id);
    }

    @Override
    public List<SysMenu> findNodes() {
        return sysMenuFeignClient.findNodes().getData();
    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        sysMenuFeignClient.doAssign(assginMenuVo);
    }

    @Override
    public List<SysMenu> toAssign(Long roleId) {
        return sysMenuFeignClient.toAssign(roleId).getData();
    }
}
