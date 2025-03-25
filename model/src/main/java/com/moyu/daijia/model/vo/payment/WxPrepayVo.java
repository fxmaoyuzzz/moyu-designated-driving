package com.moyu.daijia.model.vo.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxPrepayVo {

    @Schema(description = "公众号ID")
    private String appId;

    @Schema(description = "时间戳，自1970年以来的秒数")
    private String timeStamp;

    @Schema(description = "随机串")
    private String nonceStr;

    @Schema(description = "预支付交易会话标识")
    private String packageVal;

    @Schema(description = "微信签名方式")
    private String signType;

    @Schema(description = "微信签名")
    private String paySign;
}
