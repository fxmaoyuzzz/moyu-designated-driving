package com.moyu.daijia.model.entity.system;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "岗位")
@TableName("sys_post")
public class SysPost extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "岗位编码")
	@TableField("post_code")
	private String postCode;

	@Schema(description = "岗位名称")
	@TableField("name")
	private String name;

	@Schema(description = "显示顺序")
	@TableField("description")
	private String description;

	@Schema(description = "状态（1正常 0停用）")
	@TableField("status")
	private Integer status;

}