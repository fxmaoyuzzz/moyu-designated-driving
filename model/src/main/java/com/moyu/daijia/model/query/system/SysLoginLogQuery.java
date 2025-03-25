package com.moyu.daijia.model.query.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysLoginLogQuery {
	
	@Schema(description = "用户账号")
	private String username;

	private String createTimeBegin;
	private String createTimeEnd;

}

