package com.moyu.daijia.model.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单费用表单")
public class UpdateOrderBillForm {

    @Schema(description = "订单ID")
	private Long orderId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "实际里程")
    private BigDecimal realDistance;

    //其他费用：路桥费、停车费、其他费用、顾客好处费
    @Schema(description = "路桥费")
    private BigDecimal tollFee;

    @Schema(description = "停车费")
    private BigDecimal parkingFee;

    @Schema(description = "其他费用")
    private BigDecimal otherFee;

    @Schema(description = "顾客好处费")
    private BigDecimal favourFee;
    //其他费用：路桥费、停车费、其他费用、顾客好处费

    //订单奖励信息相关属性
    @Schema(description = "奖励规则ID")
    private Long rewardRuleId;

    @Schema(description = "奖励金额")
    private BigDecimal rewardAmount;
    //订单奖励信息相关属性

    //代驾费用信息相关属性
    @Schema(description = "费用规则ID")
    private Long feeRuleId;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "基础里程（公里）")
    private BigDecimal baseDistance;

    @Schema(description = "基础里程费（元）")
    private BigDecimal baseDistanceFee;

    @Schema(description = "超出基础里程的里程（公里）")
    private BigDecimal exceedDistance;

    @Schema(description = "超出基础里程的价格（元/公里）")
    private BigDecimal exceedDistancePrice;

    @Schema(description = "基础等时分钟（分钟）")
    private Integer baseWaitMinute;

    @Schema(description = "超出基础等时的分钟（分钟）")
    private Integer exceedWaitMinute;

    @Schema(description = "超出基础分钟的价格（元/分钟）")
    private BigDecimal exceedWaitMinutePrice;

    @Schema(description = "基础远途里程（公里）")
    private BigDecimal baseLongDistance;

    @Schema(description = "超出基础远程里程的里程（公里）")
    private BigDecimal exceedLongDistance;

    @Schema(description = "超出基础远程里程的价格（元/公里）")
    private BigDecimal exceedLongDistancePrice;
    //代驾费用信息相关属性

    //订单分账信息相关属性
    @Schema(description = "分账规则ID")
    private Long profitsharingRuleId;

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    @Schema(description = "微信支付平台费率")
    private BigDecimal paymentRate;

    @Schema(description = "微信支付平台费用")
    private BigDecimal paymentFee;

    @Schema(description = "代驾司机代缴个税税率")
    private BigDecimal driverTaxRate;

    @Schema(description = "代驾司机税率支出费用")
    private BigDecimal driverTaxFee;

    @Schema(description = "平台分账收入")
    private BigDecimal platformIncome;

    @Schema(description = "司机分账收入")
    private BigDecimal driverIncome;
    //订单分账信息相关属性
}