package com.moyu.daijia.model.vo.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class DriverLicenseOcrVo {

	@Schema(description = "驾驶证姓名")
	private String name;

	@Schema(description = "准驾车型")
	private String driverLicenseClazz;

	@Schema(description = "驾驶证证件号")
	private String driverLicenseNo;

	@Schema(description = "驾驶证有效期")
	private Date driverLicenseExpire;

	@Schema(description = "驾驶证初次领证日期")
	private Date driverLicenseIssueDate;

	@Schema(description = "驾驶证正面")
	private String driverLicenseFrontUrl;
	@Schema(description = "驾驶证正面回显")
	private String driverLicenseFrontShowUrl;

	@Schema(description = "驾驶证副页正面")
	private String driverLicenseBackUrl;
	@Schema(description = "驾驶证副页正面回显")
	private String driverLicenseBackShowUrl;

}