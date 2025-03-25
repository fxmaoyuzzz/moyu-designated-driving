package com.moyu.daijia.model.vo.map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NearByDriverVo {

    @Schema(description = "司机id")
    private Long driverId;

    @Schema(description = "距离")
    private BigDecimal distance;
}
