package com.moyu.daijia.model.entity.system;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "角色")
@TableName("sys_role")
public class SysRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	//@NotBlank(message = "角色名称不能为空")
	@Schema(description = "角色名称")
	@TableField("role_name")
	private String roleName;

	@Schema(description = "角色编码")
	@TableField("role_code")
	private String roleCode;

	@Schema(description = "描述")
	@TableField("description")
	private String description;

}

