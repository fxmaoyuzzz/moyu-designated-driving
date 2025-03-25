package com.moyu.daijia.model.query.system;

import lombok.Data;

@Data
public class SysPostQuery {
	
	//@Schema(description = "岗位编码")
	private String postCode;

	//@Schema(description = "岗位名称")
	private String name;

	//@Schema(description = "状态（1正常 0停用）")
	private Boolean status;


}

