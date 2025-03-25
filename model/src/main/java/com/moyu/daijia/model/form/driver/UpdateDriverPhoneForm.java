package com.moyu.daijia.model.form.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateDriverPhoneForm {

    @Schema(description = "司机Id")
    private Long driverId;


    @Schema(description = "手机号码")
    private String phone;
}
