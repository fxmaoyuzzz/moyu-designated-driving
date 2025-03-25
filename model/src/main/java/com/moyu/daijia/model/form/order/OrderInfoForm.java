package com.moyu.daijia.model.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "OrderInfo")
public class OrderInfoForm {


    @Schema(description = "客户ID")
	private Long customerId;

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

    @Schema(description = "顾客好处费")
    private BigDecimal favourFee;

    @Schema(description = "订单备注信息")
	private String remark;


    //预期费用信息
    @Schema(description = "预估订单费用")
    private BigDecimal expectAmount;

    @Schema(description = "预估里程")
    private BigDecimal expectDistance;

}