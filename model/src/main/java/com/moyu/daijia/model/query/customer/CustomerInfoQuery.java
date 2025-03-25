package com.moyu.daijia.model.query.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页结果包装
 *
 * @author itcast
 */
@Data
@Schema(description = "乘客信息查询实体")
public class CustomerInfoQuery {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "状态")
    private Integer status;

    private String createTimeBegin;
    private String createTimeEnd;

}
