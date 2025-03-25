package com.moyu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderListVo {

	@Schema(description = "id")
	private Long id;

	@Schema(description = "订单号")
	private String orderNo;

	@Schema(description = "起始地点")
	private String startLocation;

	@Schema(description = "结束地点")
	private String endLocation;

	@Schema(description = "订单金额")
	private BigDecimal amount;

	@Schema(description = "下单时间")
	private Date createTime;

	@Schema(description = "订单状态")
	private Integer status;
}