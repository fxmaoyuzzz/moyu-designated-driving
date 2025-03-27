package com.moyu.daijia.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    FEIGN_FAIL(207, "远程调用失败"),
    UPDATE_ERROR(204, "数据更新失败"),

    ARGUMENT_VALID_ERROR(210, "参数校验异常"),
    SIGN_ERROR(300, "签名错误"),
    SIGN_OVERDUE(301, "签名已过期"),
    VALIDATECODE_ERROR(218, "验证码错误"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    ACCOUNT_ERROR(214, "账号不正确"),
    PASSWORD_ERROR(215, "密码不正确"),
    PHONE_CODE_ERROR(215, "手机验证码不正确"),
    LOGIN_MOBLE_ERROR(216, "账号不正确"),
    ACCOUNT_STOP(216, "账号已停用"),
    NODE_ERROR(217, "该节点下有子节点，不可以删除"),

    COB_NEW_ORDER_FAIL(217, "抢单失败"),
    MAP_FAIL(217, "地图服务调用失败"),
    MAP_SUCCESS(0, "地图服务调用成功"),
    PROFITSHARING_FAIL(217, "分账调用失败"),
    NO_START_SERVICE(217, "未开启代驾服务，不能更新位置信息"),
    DRIVER_SERVICE_OPEN(1, "司机代驾服务状态开启"),
    DRIVER_SERVICE_SHUTDOWN(0, "司机代驾服务状态关闭"),
    DRIVER_START_LOCATION_DISTION_ERROR(217, "距离代驾起始点1公里以内才能确认"),
    DRIVER_END_LOCATION_DISTION_ERROR(217, "距离代驾终点2公里以内才能确认"),
    IMAGE_AUDITION_FAIL(217, "图片审核不通过"),
    AUTH_ERROR(217, "认证通过后才可以开启代驾服务"),
    FACE_ERROR(250, "当日未进行人脸识别"),

    COUPON_EXPIRE(250, "优惠券已过期"),
    COUPON_LESS(250, "优惠券库存不足"),
    COUPON_USER_LIMIT(250, "超出领取数量"),
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
