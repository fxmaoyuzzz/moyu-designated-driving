package com.moyu.daijia.model.entity.order;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "OrderBill")
@TableName("order_bill")
public class OrderBill extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
	@TableField("order_id")
	private Long orderId;

	@Schema(description = "规则ID")
	@TableField("fee_rule_id")
	private Long feeRuleId;

    @Schema(description = "总金额")
	@TableField("total_amount")
	private BigDecimal totalAmount;

    @Schema(description = "应付款金额")
	@TableField("pay_amount")
	private BigDecimal payAmount;

    @Schema(description = "里程费")
	@TableField("distance_fee")
	private BigDecimal distanceFee;

    @Schema(description = "等时费用")
	@TableField("wait_fee")
	private BigDecimal waitFee;

    @Schema(description = "路桥费")
	@TableField("toll_fee")
	private BigDecimal tollFee;

    @Schema(description = "停车费")
	@TableField("parking_fee")
	private BigDecimal parkingFee;

    @Schema(description = "其他费用")
	@TableField("other_fee")
	private BigDecimal otherFee;

    @Schema(description = "远程费")
	@TableField("long_distance_fee")
	private BigDecimal longDistanceFee;

    @Schema(description = "顾客好处费")
	@TableField("favour_fee")
	private BigDecimal favourFee;

	@Schema(description = "系统奖励费")
	@TableField("reward_fee")
	private BigDecimal rewardFee;

	@Schema(description = "系统奖励规则id")
	@TableField("reward_rule_id")
	private Long rewardRuleId;

	@Schema(description = "优惠券金额")
	@TableField("coupon_amount")
	private BigDecimal couponAmount;

    @Schema(description = "基础里程（公里）")
	@TableField("base_distance")
	private BigDecimal baseDistance;

    @Schema(description = "基础里程费")
	@TableField("base_distance_fee")
	private BigDecimal baseDistanceFee;

    @Schema(description = "超出基础里程的里程（公里）")
	@TableField("exceed_distance")
	private BigDecimal exceedDistance;

    @Schema(description = "超出基础里程的价格")
	@TableField("exceed_distance_price")
	private BigDecimal exceedDistancePrice;

    @Schema(description = "基础等时分钟")
	@TableField("base_wait_minute")
	private Integer baseWaitMinute;

    @Schema(description = "超出基础等时的分钟")
	@TableField("exceed_wait_minute")
	private Integer exceedWaitMinute;

    @Schema(description = "超出基础分钟的价格")
	@TableField("exceed_wait_minute_price")
	private BigDecimal exceedWaitMinutePrice;

    @Schema(description = "基础远途里程（公里）")
	@TableField("base_long_distance")
	private BigDecimal baseLongDistance;

    @Schema(description = "超出基础远程里程的里程")
	@TableField("exceed_long_distance")
	private BigDecimal exceedLongDistance;

    @Schema(description = "超出基础远程里程的价格")
	@TableField("exceed_long_distance_price")
	private BigDecimal exceedLongDistancePrice;

}