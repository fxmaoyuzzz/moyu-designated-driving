package com.moyu.daijia.model.form.map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateDriverLocationForm {

    @Schema(description = "司机id")
    private Long driverId;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "伟度")
    private BigDecimal latitude;

}
