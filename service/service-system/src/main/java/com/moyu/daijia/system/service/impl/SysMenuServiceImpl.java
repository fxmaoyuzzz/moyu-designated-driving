package com.moyu.daijia.system.service.impl;

import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.model.entity.system.SysMenu;
import com.moyu.daijia.model.entity.system.SysRoleMenu;
import com.moyu.daijia.model.vo.system.AssginMenuVo;
import com.moyu.daijia.model.vo.system.MetaVo;
import com.moyu.daijia.model.vo.system.RouterVo;
import com.moyu.daijia.system.helper.MenuHelper;
import com.moyu.daijia.system.mapper.SysMenuMapper;
import com.moyu.daijia.system.mapper.SysRoleMenuMapper;
import com.moyu.daijia.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //全部权限列表
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;

        //构建树形数据
        List<SysMenu> result = MenuHelper.buildTree(sysMenuList);
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        long count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new MoyuException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.deleteById(id);
        return false;
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //全部权限列表
        List<SysMenu> allSysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        //根据角色id获取角色权限
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        //转换给角色id与角色权限对应Map对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(e -> e.getMenuId()).collect(Collectors.toList());

        allSysMenuList.forEach(permission -> {
            if (menuIdList.contains(permission.getId())) {
                permission.setSelect(true);
            } else {
                permission.setSelect(false);
            }
        });

        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);
        return sysMenuList;
    }

    @Transactional
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId()));

        for (Long menuId : assginMenuVo.getMenuIdList()) {
            if (null == menuId) continue;
            SysRoleMenu rolePermission = new SysRoleMenu();
            rolePermission.setRoleId(assginMenuVo.getRoleId());
            rolePermission.setMenuId(menuId);
            sysRoleMenuMapper.insert(rolePermission);
        }
    }

    @Override
    public List<RouterVo> findUserMenuList(Long userId) {
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {
            sysMenuList = sysMenuMapper.findListByUserId(userId);
        }
        //构建树形数据
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);

        List<RouterVo> routerVoList = this.buildMenus(sysMenuTreeList);
        return routerVoList;
    }

    /**
     * 根据菜单构建路由
     * @param menus
     * @return
     */
    private List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            //router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            Boolean isHide = menu.getIsHide().intValue() == 1 ? true : false;
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon(), menu.getActiveMenu(), isHide));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if(menu.getType().intValue() == 1) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> StringUtils.hasText(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    //hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    Boolean isHide1 = hiddenMenu.getIsHide().intValue() == 1 ? true : false;
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon(), menu.getActiveMenu(), isHide1));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildMenus(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findUserPermsList(Long userId) {
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            sysMenuList = sysMenuMapper.findListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }
}
