package com.moyu.daijia.model.entity.system;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单")
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Schema(description = "所属上级")
	@TableField("parent_id")
	private Long parentId;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(description = "类型(1:菜单,2:按钮)")
	@TableField("type")
	private Integer type;

	@Schema(description = "路由地址")
	@TableField("path")
	private String path;

	@Schema(description = "组件路径")
	@TableField("component")
	private String component;

	@Schema(description = "权限标识")
	@TableField("perms")
	private String perms;

	@Schema(description = "图标")
	@TableField("icon")
	private String icon;

	@Schema(description = "排序")
	@TableField("sort_value")
	private Integer sortValue;

	@Schema(description = "状态(0:禁止,1:正常)")
	@TableField("status")
	private Integer status;

	@Schema(description = "高亮的路径")
	@TableField("active_menu")
	private String activeMenu;

	@Schema(description = "是否隐藏")
	@TableField("is_hide")
	private Integer isHide;

	// 下级列表
	@TableField(exist = false)
	private List<SysMenu> children;
	//是否选中
	@TableField(exist = false)
	private boolean isSelect;
}

