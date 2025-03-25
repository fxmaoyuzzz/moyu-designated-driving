package com.moyu.daijia.model.vo.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DriverLoginVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "微信openId")
    private String wxOpenId;

    @Schema(description = "客户昵称")
    private String nickname;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "头像")
    private String avatarUrl;

    @Schema(description = "是否建档人脸识别")
    private Boolean isArchiveFace;

    @Schema(description = "认证状态 0:未认证 1：审核中 2：认证通过 -1：认证未通过")
    private Integer authStatus;

}