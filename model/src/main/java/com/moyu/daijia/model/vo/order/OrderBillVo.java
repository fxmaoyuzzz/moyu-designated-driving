package com.moyu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBillVo {

	@Schema(description = "总金额")
	private BigDecimal totalAmount;

	@Schema(description = "应付款金额")
	private BigDecimal payAmount;

	@Schema(description = "里程费")
	private BigDecimal distanceFee;

	@Schema(description = "等时费用")
	private BigDecimal waitFee;

	@Schema(description = "路桥费")
	private BigDecimal tollFee;

	@Schema(description = "停车费")
	private BigDecimal parkingFee;

	@Schema(description = "其他费用")
	private BigDecimal otherFee;

	@Schema(description = "远程费")
	private BigDecimal longDistanceFee;

	@Schema(description = "顾客好处费")
	private BigDecimal favourFee;

	@Schema(description = "系统奖励费")
	private BigDecimal rewardFee;

	@Schema(description = "优惠券金额")
	private BigDecimal couponAmount;

	@Schema(description = "基础里程（公里）")
	private BigDecimal baseDistance;

	@Schema(description = "基础里程费")
	private BigDecimal baseDistanceFee;

	@Schema(description = "超出基础里程的里程（公里）")
	private BigDecimal exceedDistance;

	@Schema(description = "超出基础里程的价格")
	private BigDecimal exceedDistancePrice;

	@Schema(description = "基础等时分钟")
	private Integer baseWaitMinute;

	@Schema(description = "超出基础等时的分钟")
	private Integer exceedWaitMinute;

	@Schema(description = "超出基础分钟的价格")
	private BigDecimal exceedWaitMinutePrice;

	@Schema(description = "基础远途里程（公里）")
	private BigDecimal baseLongDistance;

	@Schema(description = "超出基础远程里程的里程")
	private BigDecimal exceedLongDistance;

	@Schema(description = "超出基础远程里程的价格")
	private BigDecimal exceedLongDistancePrice;

}