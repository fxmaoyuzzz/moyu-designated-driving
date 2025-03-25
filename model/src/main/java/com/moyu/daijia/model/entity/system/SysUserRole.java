package com.moyu.daijia.model.entity.system;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户角色")
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Schema(description = "角色id")
	@TableField("role_id")
	private Long roleId;

	@Schema(description = "用户id")
	@TableField("user_id")
	private Long userId;
}

