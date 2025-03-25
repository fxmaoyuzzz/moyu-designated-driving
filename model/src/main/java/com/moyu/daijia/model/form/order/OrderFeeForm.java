package com.moyu.daijia.model.form.order;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单费用表单")
public class OrderFeeForm {

    @Schema(description = "订单ID")
	private Long orderId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "路桥费")
    private BigDecimal tollFee;

    @Schema(description = "停车费")
    private BigDecimal parkingFee;

    @Schema(description = "其他费用")
    @TableField("other_fee")
    private BigDecimal otherFee;

}