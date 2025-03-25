package com.moyu.daijia.model.form.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateWxPhoneForm {

	@Schema(description = "客户Id")
	private Long customerId;

	private String code;

}