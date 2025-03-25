package com.moyu.daijia.model.form.rules;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitsharingRuleRequest {

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    @Schema(description = "当天完成订单个数")
    private Long orderNum;

}
