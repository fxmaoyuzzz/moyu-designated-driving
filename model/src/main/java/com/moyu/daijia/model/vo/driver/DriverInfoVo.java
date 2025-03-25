package com.moyu.daijia.model.vo.driver;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DriverInfoVo {

	@Schema(description = "微信openId")
	private String wxOpenId;

	@Schema(description = "司机名称")
	private String name;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "性别")
	private String gender;

	@Schema(description = "头像")
	private String avatarUrl;

	@Schema(description = "驾龄")
	private Integer driverLicenseAge;

	@Schema(description = "订单量统计")
	private Integer orderCount;

	@Schema(description = "评分")
	private BigDecimal score;
}