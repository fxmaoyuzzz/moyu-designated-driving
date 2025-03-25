package com.moyu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRewardVo {

	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "司机id")
	private Long driverId;

	@Schema(description = "系统奖励")
	private BigDecimal rewardFee;

}