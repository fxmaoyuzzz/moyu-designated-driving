package com.moyu.daijia.model.entity.driver;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DriverAccountDetail")
@TableName("driver_account_detail")
public class DriverAccountDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "司机id")
	@TableField("driver_id")
	private Long driverId;

    @Schema(description = "交易内容")
	@TableField("content")
	private String content;

    @Schema(description = "交易类型：1201-充值 1202-锁定 1203-解锁 1204-消费")
	@TableField("trade_type")
	private String tradeType;

    @Schema(description = "金额")
	@TableField("amount")
	private BigDecimal amount;

    @Schema(description = "交易号")
	@TableField("trade_no")
	private String tradeNo;

}