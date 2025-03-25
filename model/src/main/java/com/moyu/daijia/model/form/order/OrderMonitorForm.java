package com.moyu.daijia.model.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单监控")
public class OrderMonitorForm {

    @Schema(description = "订单ID")
	private Long orderId;

    @Schema(description = "监控内容")
    private String content;

}