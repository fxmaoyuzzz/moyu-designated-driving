package com.moyu.daijia.model.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginVo {

	@Schema(description = "登录用户第Id")
	private Long userId;

	@Schema(description = "微信openId")
	private String wxOpenId;

	@Schema(description = "客户昵称")
	private String nickname;

	@Schema(description = "头像")
	private String avatarUrl;
}