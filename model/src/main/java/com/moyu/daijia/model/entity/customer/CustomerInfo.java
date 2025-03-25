package com.moyu.daijia.model.entity.customer;

import com.moyu.daijia.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "CustomerInfo")
public class CustomerInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "微信openId")
	private String wxOpenId;

	@Schema(description = "客户昵称")
	private String nickname;

	@Schema(description = "性别")
	private String gender;

	@Schema(description = "头像")
	private String avatarUrl;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "1有效，2禁用")
	private Integer status;

}