package com.moyu.daijia.model.form.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateCustomerPhoneForm {

    @Schema(description = "客户Id")
    private Long customerId;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "手机验证码")
    private String code;
}
