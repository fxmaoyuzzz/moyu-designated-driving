package com.moyu.daijia.model.entity.order;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "OrderInfo")
@TableName("order_info")
public class OrderInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
	@TableField("customer_id")
	private Long customerId;

    @Schema(description = "订单号")
	@TableField("order_no")
	private String orderNo;

    @Schema(description = "起始地点")
	@TableField("start_location")
	private String startLocation;

    @Schema(description = "起始地点经度")
	@TableField("start_point_longitude")
	private BigDecimal startPointLongitude;

    @Schema(description = "起始点伟度")
	@TableField("start_point_latitude")
	private BigDecimal startPointLatitude;

    @Schema(description = "结束地点")
	@TableField("end_location")
	private String endLocation;

    @Schema(description = "结束地点经度")
	@TableField("end_point_longitude")
	private BigDecimal endPointLongitude;

    @Schema(description = "结束地点经度")
	@TableField("end_point_latitude")
	private BigDecimal endPointLatitude;

    @Schema(description = "预估里程")
	@TableField("expect_distance")
	private BigDecimal expectDistance;

    @Schema(description = "实际里程")
	@TableField("real_distance")
	private BigDecimal realDistance;

    @Schema(description = "预估订单金额")
	@TableField("expect_amount")
	private BigDecimal expectAmount;

    @Schema(description = "实际订单金额")
	@TableField("real_amount")
	private BigDecimal realAmount;

    @Schema(description = "顾客好处费")
	@TableField("favour_fee")
	private BigDecimal favourFee;

    @Schema(description = "司机ID")
	@TableField("driver_id")
	private Long driverId;

    @Schema(description = "司机接单时间")
	@TableField("accept_time")
	private Date acceptTime;

    @Schema(description = "司机到达时间")
	@TableField("arrive_time")
	private Date arriveTime;

    @Schema(description = "开始服务时间")
	@TableField("start_service_time")
	private Date startServiceTime;

    @Schema(description = "结束服务时间")
	@TableField("end_service_time")
	private Date endServiceTime;

    @Schema(description = "微信付款时间")
	@TableField("pay_time")
	private Date payTime;

    @Schema(description = "订单取消规则ID")
	@TableField("cancel_rule_id")
	private Long cancelRuleId;

    @Schema(description = "车牌号")
	@TableField("car_license")
	private String carLicense;

    @Schema(description = "车型")
	@TableField("car_type")
	private String carType;

	@Schema(description = "司机到达拍照：车前照")
	@TableField("car_front_url")
	private String carFrontUrl;

	@Schema(description = "司机到达拍照：车后照")
	@TableField("car_back_url")
	private String carBackUrl;

	@Schema(description = "微信支付订单号")
	@TableField("transaction_id")
	private String transactionId;

    @Schema(description = "订单状态：1等待接单，2已接单，3司机已到达，4开始代驾，5结束代驾，6未付款，7已付款，8订单已结束，9顾客撤单，10司机撤单，11事故关闭，12其他")
	@TableField("status")
	private Integer status;

    @Schema(description = "订单备注信息")
	@TableField("remark")
	private String remark;

}