package com.moyu.daijia.model.form.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateCustomerInfoForm {

    @Schema(description = "客户Id")
    private Long customerId;

    @Schema(description = "客户昵称")
    private String nickname;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "头像")
    private String avatarUrl;
}
