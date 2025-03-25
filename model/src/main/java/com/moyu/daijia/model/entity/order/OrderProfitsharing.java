package com.moyu.daijia.model.entity.order;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "OrderProfitsharing")
@TableName("order_profitsharing")
public class OrderProfitsharing extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
	@TableField("order_id")
	private Long orderId;

    @Schema(description = "规则ID")
	@TableField("rule_id")
	private Long ruleId;

    @Schema(description = "订单金额")
	@TableField("order_amount")
	private BigDecimal orderAmount;

    @Schema(description = "微信支付平台费率")
	@TableField("payment_rate")
	private BigDecimal paymentRate;

    @Schema(description = "微信支付平台费用")
	@TableField("payment_fee")
	private BigDecimal paymentFee;

    @Schema(description = "代驾司机代缴个税税率")
	@TableField("driver_tax_rate")
	private BigDecimal driverTaxRate;

    @Schema(description = "代驾司机税率支出费用")
	@TableField("driver_tax_fee")
	private BigDecimal driverTaxFee;

    @Schema(description = "平台分账收入")
	@TableField("platform_income")
	private BigDecimal platformIncome;

    @Schema(description = "司机分账收入")
	@TableField("driver_income")
	private BigDecimal driverIncome;

    @Schema(description = "分账状态，1未分账，2已分账")
	@TableField("status")
	private Integer status;

}