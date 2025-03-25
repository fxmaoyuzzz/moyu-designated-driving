package com.moyu.daijia.model.query.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页结果包装
 *
 * @author itcast
 */
@Data
@Schema(description = "订单信息查询实体")
public class OrderInfoQuery {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "状态")
    private Integer status;

    private String createTimeBegin;
    private String createTimeEnd;

}
