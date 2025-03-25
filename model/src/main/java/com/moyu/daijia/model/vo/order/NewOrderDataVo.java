package com.moyu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewOrderDataVo {

	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "起始地点")
	private String startLocation;

	@Schema(description = "结束地点")
	private String endLocation;

	@Schema(description = "预估订单金额")
	private BigDecimal expectAmount;

	@Schema(description = "预估里程")
	private BigDecimal expectDistance;

	@Schema(description = "到你的距离")
	private BigDecimal distance;

	@Schema(description = "预估时间")
	private BigDecimal expectTime;

	@Schema(description = "顾客好处费")
	private BigDecimal favourFee;

	@Schema(description = "下单时间")
	private Date createTime;
}