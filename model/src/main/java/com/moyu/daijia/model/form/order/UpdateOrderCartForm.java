package com.moyu.daijia.model.form.order;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新订单车辆表单")
public class UpdateOrderCartForm {

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "司机ID")
    private Long driverId;

    @NotEmpty(message = "车牌号不能为空")
    @Schema(description = "车牌号")
    @TableField("car_license")
    private String carLicense;

    @Schema(description = "车型")
    @TableField("car_type")
    private String carType;

    @NotEmpty(message = "车前照不能为空")
    @Schema(description = "司机到达拍照：车前照")
    @TableField("car_front_url")
    private String carFrontUrl;

    @NotEmpty(message = "车后照不能为空")
    @Schema(description = "司机到达拍照：车后照")
    @TableField("car_back_url")
    private String carBackUrl;

}