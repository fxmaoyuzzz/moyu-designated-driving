package com.moyu.daijia.model.entity.payment;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ProfitsharingInfo")
@TableName("profitsharing_info")
public class ProfitsharingInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "司机id")
	@TableField("driver_id")
	private Long driverId;

    @Schema(description = "订单号")
	@TableField("order_no")
	private String orderNo;

    @Schema(description = "微信支付订单号")
	@TableField("transaction_id")
	private String transactionId;

    @Schema(description = "商户分账单号")
	@TableField("out_trade_no")
	private String outTradeNo;

    @Schema(description = "司机分账金额")
	@TableField("amount")
	private String amount;

    @Schema(description = "分账单状态 PROCESSING：处理中  FINISHED：分账完成")
	@TableField("state")
	private String state;

    @Schema(description = "返回信息")
	@TableField("respone_content")
	private String responeContent;

}