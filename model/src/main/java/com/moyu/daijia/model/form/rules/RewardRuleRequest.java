package com.moyu.daijia.model.form.rules;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RewardRuleRequest {

    @Schema(description = "代驾时间")
    private String startTime;

    @Schema(description = "订单个数")
    private Long orderNum;

}
