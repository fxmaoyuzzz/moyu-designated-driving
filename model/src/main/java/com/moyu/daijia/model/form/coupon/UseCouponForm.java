package com.moyu.daijia.model.form.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UseCouponForm {

    @Schema(description = "乘客id")
    private Long customerId;

    @Schema(description = "乘客优惠券id")
    private Long customerCouponId;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

}
