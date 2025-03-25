package com.moyu.daijia.model.entity.driver;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "DriverInfo")
@TableName("driver_info")
public class DriverInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Schema(description = "微信openId")
	@TableField("wx_open_id")
	private String wxOpenId;

    @Schema(description = "昵称")
	@TableField("nickname")
	private String nickname;

    @Schema(description = "头像")
	@TableField("avatar_url")
	private String avatarUrl;

    @Schema(description = "电话")
	@TableField("phone")
	private String phone;

    @Schema(description = "姓名")
	@TableField("name")
	private String name;

    @Schema(description = "性别")
	@TableField("gender")
	private String gender;

    @Schema(description = "生日")
	@TableField("birthday")
	private Date birthday;

    @Schema(description = "身份证号码")
	@TableField("idcard_no")
	private String idcardNo;

    @Schema(description = "身份证地址")
	@TableField("idcard_address")
	private String idcardAddress;

    @Schema(description = "身份证有效期")
	@TableField("idcard_expire")
	private Date idcardExpire;

    @Schema(description = "身份证正面")
	@TableField("idcard_front_url")
	private String idcardFrontUrl;

    @Schema(description = "身份证背面")
	@TableField("idcard_back_url")
	private String idcardBackUrl;

    @Schema(description = "手持身份证")
	@TableField("idcard_hand_url")
	private String idcardHandUrl;

    @Schema(description = "准驾车型")
	@TableField("driver_license_class")
	private String driverLicenseClazz;

	@Schema(description = "驾驶证证件号")
	@TableField("driver_license_no")
	private String driverLicenseNo;

    @Schema(description = "驾驶证有效期")
	@TableField("driver_license_expire")
	private Date driverLicenseExpire;

    @Schema(description = "驾驶证初次领证日期")
	@TableField("driver_license_issue_date")
	private Date driverLicenseIssueDate;

    @Schema(description = "驾驶证正面")
	@TableField("driver_license_front_url")
	private String driverLicenseFrontUrl;

    @Schema(description = "行驶证副页正面")
	@TableField("driver_license_back_url")
	private String driverLicenseBackUrl;

    @Schema(description = "手持驾驶证")
	@TableField("driver_license_hand_url")
	private String driverLicenseHandUrl;

    @Schema(description = "紧急联系人")
	@TableField("contact_name")
	private String contactName;

    @Schema(description = "紧急联系人电话")
	@TableField("contact_phone")
	private String contactPhone;

    @Schema(description = "紧急联系人关系")
	@TableField("contact_relationship")
	private String contactRelationship;

    @Schema(description = "腾讯云人脸模型id")
	@TableField("face_model_id")
	private String faceModelId;

	@Schema(description = "司机工号")
	@TableField("job_no")
	private String jobNo;

	@Schema(description = "订单量统计")
	@TableField("order_count")
	private Integer orderCount;

	@Schema(description = "评分")
	@TableField("score")
	private BigDecimal score;

    @Schema(description = "认证状态")
	@TableField("auth_status")
	private Integer authStatus;

    @Schema(description = "状态，1正常，2禁用")
	@TableField("status")
	private Integer status;

}