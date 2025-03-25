package com.moyu.daijia.model.vo.order;

import com.moyu.daijia.model.vo.customer.CustomerInfoVo;
import com.moyu.daijia.model.vo.driver.DriverInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderInfoVo {

	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "订单号")
	private String orderNo;

	@Schema(description = "起始地点")
	private String startLocation;

	@Schema(description = "起始地点经度")
	private BigDecimal startPointLongitude;

	@Schema(description = "起始点伟度")
	private BigDecimal startPointLatitude;

	@Schema(description = "结束地点")
	private String endLocation;

	@Schema(description = "结束地点经度")
	private BigDecimal endPointLongitude;

	@Schema(description = "结束地点经度")
	private BigDecimal endPointLatitude;

	@Schema(description = "订单状态")
	private Integer status;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "订单账单信息")
	private OrderBillVo orderBillVo;

	@Schema(description = "分账信息")
	private OrderProfitsharingVo orderProfitsharingVo;

	@Schema(description = "司机信息")
	private DriverInfoVo driverInfoVo;

	@Schema(description = "乘客信息")
	private CustomerInfoVo customerInfoVo;

}