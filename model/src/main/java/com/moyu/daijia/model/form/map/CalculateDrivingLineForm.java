package com.moyu.daijia.model.form.map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculateDrivingLineForm {

    @Schema(description = "起始地点经度")
    private BigDecimal startPointLongitude;

    @Schema(description = "起始点伟度")
    private BigDecimal startPointLatitude;

    @Schema(description = "结束地点经度")
    private BigDecimal endPointLongitude;

    @Schema(description = "结束地点经度")
    private BigDecimal endPointLatitude;
}
