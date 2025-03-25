package com.moyu.daijia.model.form.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateWxPaymentForm {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "乘客优惠券id")
    private Long customerCouponId;

    @Schema(description = "乘客id")
    private Long customerId;
}
