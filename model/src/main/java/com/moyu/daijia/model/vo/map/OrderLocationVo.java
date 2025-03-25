package com.moyu.daijia.model.vo.map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderLocationVo {

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "伟度")
    private BigDecimal latitude;
}
