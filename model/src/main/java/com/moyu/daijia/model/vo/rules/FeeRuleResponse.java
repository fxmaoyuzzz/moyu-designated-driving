package com.moyu.daijia.model.vo.rules;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeRuleResponse {


    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "里程费")
    private BigDecimal distanceFee;

    @Schema(description = "等时费用")
    private BigDecimal waitFee;

    @Schema(description = "远程费")
    private BigDecimal longDistanceFee;

    @Schema(description = "基础里程（公里）")
    private BigDecimal baseDistance;

    @Schema(description = "基础里程费（元）")
    private BigDecimal baseDistanceFee;

    @Schema(description = "超出基础里程的里程（公里）")
    private BigDecimal exceedDistance;

    @Schema(description = "超出基础里程的价格（元/公里）")
    private BigDecimal exceedDistancePrice;

    @Schema(description = "基础等时分钟（分钟）")
    private Integer baseWaitMinute;

    @Schema(description = "超出基础等时的分钟（分钟）")
    private Integer exceedWaitMinute;

    @Schema(description = "超出基础分钟的价格（元/分钟）")
    private BigDecimal exceedWaitMinutePrice;

    @Schema(description = "基础远途里程（公里）")
    private BigDecimal baseLongDistance;

    @Schema(description = "超出基础远程里程的里程（公里）")
    private BigDecimal exceedLongDistance;

    @Schema(description = "超出基础远程里程的价格（元/公里）")
    private BigDecimal exceedLongDistancePrice;
}
