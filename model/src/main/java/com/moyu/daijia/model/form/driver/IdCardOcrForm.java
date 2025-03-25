package com.moyu.daijia.model.form.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IdCardOcrForm {

    @Schema(description = "身份证Base64字符串")
    private String idCardBase64;
}
