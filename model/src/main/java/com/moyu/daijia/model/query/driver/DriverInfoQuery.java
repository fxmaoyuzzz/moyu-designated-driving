package com.moyu.daijia.model.query.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页结果包装
 *
 * @author itcast
 */
@Data
@Schema(description = "司机信息查询实体")
public class DriverInfoQuery {

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "状态")
    private Integer status;

    private String createTimeBegin;
    private String createTimeEnd;

}
