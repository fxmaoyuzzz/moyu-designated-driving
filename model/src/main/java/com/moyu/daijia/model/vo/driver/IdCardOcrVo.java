package com.moyu.daijia.model.vo.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class IdCardOcrVo {

	@Schema(description = "姓名")
	private String name;

	@Schema(description = "性别 1：男 2：女")
	private String gender;

	@Schema(description = "生日")
	private Date birthday;

	@Schema(description = "身份证号码")
	private String idcardNo;

	@Schema(description = "身份证地址")
	private String idcardAddress;

	@Schema(description = "身份证有效期")
	private Date idcardExpire;

	@Schema(description = "身份证正面")
	private String idcardFrontUrl;
	@Schema(description = "身份证正面回显")
	private String idcardFrontShowUrl;

	@Schema(description = "身份证背面")
	private String idcardBackUrl;
	@Schema(description = "身份证背面回显")
	private String idcardBackShowUrl;

}