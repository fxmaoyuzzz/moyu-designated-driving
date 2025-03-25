package com.moyu.daijia.model.vo.system;

import lombok.Data;

/**
 * 路由显示信息
 *
 */
@Data
public class MetaVo
{
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 高亮的路径
     */
    private String activeMenu;

    /**
     * 是否隐藏
     */
    private Boolean isHide;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon, String activeMenu, Boolean isHide)
    {
        this.title = title;
        this.icon = icon;
        if(null == activeMenu) activeMenu = "";
        this.activeMenu = activeMenu;
        this.isHide = isHide;
    }

}

