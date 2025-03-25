package com.moyu.daijia.model.entity.coupon;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "CouponInfo")
@TableName("coupon_info")
public class CouponInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "优惠卷类型 1 现金券 2 折扣")
	@TableField("coupon_type")
	private Integer couponType;

    @Schema(description = "优惠卷名字")
	@TableField("name")
	private String name;

    @Schema(description = "金额")
	@TableField("amount")
	private BigDecimal amount;

    @Schema(description = "折扣：取值[1 到 10]")
	@TableField("discount")
	private BigDecimal discount;

    @Schema(description = "使用门槛 0->没门槛")
	@TableField("condition_amount")
	private BigDecimal conditionAmount;

    @Schema(description = "发行数量")
	@TableField("publish_count")
	private Integer publishCount;

    @Schema(description = "每人限领张数")
	@TableField("per_limit")
	private Integer perLimit;

    @Schema(description = "已使用数量")
	@TableField("use_count")
	private Integer useCount;

    @Schema(description = "领取数量")
	@TableField("receive_count")
	private Integer receiveCount;

    @Schema(description = "过期时间")
	@TableField("expire_time")
	private Date expireTime;

    @Schema(description = "优惠券描述")
	@TableField("description")
	private String description;

    @Schema(description = "状态[0-未发布，1-已发布， -1-已过期]")
	@TableField("status")
	private Integer status;

}