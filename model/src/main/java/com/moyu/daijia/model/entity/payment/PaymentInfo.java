package com.moyu.daijia.model.entity.payment;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "PaymentInfo")
@TableName("payment_info")
public class PaymentInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "乘客微信openid")
	@TableField("customer_open_id")
	private String customerOpenId;

	@Schema(description = "司机微信openid")
	@TableField("driver_open_id")
	private String driverOpenId;

	@Schema(description = "订单号")
	@TableField("order_no")
	private String orderNo;

	@Schema(description = "付款方式：1101-微信 1102-支付宝")
	@TableField("pay_way")
	private Integer payWay;

	@Schema(description = "交易编号（微信或支付）")
	@TableField("transaction_id")
	private String transactionId;

	@Schema(description = "支付金额")
	@TableField("amount")
	private BigDecimal amount;

	@Schema(description = "交易内容")
	@TableField("content")
	private String content;

	@Schema(description = "支付状态：0-未支付 1-已支付")
	@TableField("payment_status")
	private Integer paymentStatus;

	@Schema(description = "回调时间")
	@TableField("callback_time")
	private Date callbackTime;

	@Schema(description = "回调信息")
	@TableField("callback_content")
	private String callbackContent;

}