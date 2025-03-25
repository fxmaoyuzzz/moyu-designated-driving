package com.moyu.daijia.model.form.rules;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeRuleRequest {

    @Schema(description = "代驾里程")
    private BigDecimal distance;

    @Schema(description = "代驾时间")
    private String startTime;

    @Schema(description = "等候分钟")
    private Integer waitMinute;

}
