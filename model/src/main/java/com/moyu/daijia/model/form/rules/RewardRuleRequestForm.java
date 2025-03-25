package com.moyu.daijia.model.form.rules;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class RewardRuleRequestForm {

    @Schema(description = "代驾时间")
    private Date startTime;

    @Schema(description = "订单个数")
    private Long orderNum;

}
