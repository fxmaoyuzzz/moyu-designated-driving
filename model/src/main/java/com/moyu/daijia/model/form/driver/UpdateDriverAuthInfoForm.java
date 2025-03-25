package com.moyu.daijia.model.form.driver;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateDriverAuthInfoForm {

    @Schema(description = "司机Id")
    private Long driverId;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatarUrl;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别")
    @TableField("gender")
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

    @Schema(description = "身份证背面")
    private String idcardBackUrl;

    @Schema(description = "手持身份证")
    private String idcardHandUrl;

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

    @Schema(description = "行驶证副页正面")
    private String driverLicenseBackUrl;

    @Schema(description = "手持驾驶证")
    private String driverLicenseHandUrl;

    @Schema(description = "紧急联系人")
    private String contactName;

    @Schema(description = "紧急联系人电话")
    private String contactPhone;

    @Schema(description = "紧急联系人关系")
    private String contactRelationship;
}
