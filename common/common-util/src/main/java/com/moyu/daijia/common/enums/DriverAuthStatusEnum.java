package com.moyu.daijia.common.enums;

import lombok.Getter;

/**
 * @author fxmao
 * @date 2025-03-26 17:23
 */
@Getter

public enum DriverAuthStatusEnum {

    NOT_CERTIFIED(0, "未认证"),
    PENDING_REVIEW(1, "审核中"),
    CERTIFIED(2, "认证通过"),
    CERTIFICATION_FAILED(-1, "认证未通过");

    private final int code;
    private final String description;

    DriverAuthStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public static DriverAuthStatusEnum fromCode(int code) {
        for (DriverAuthStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的认证状态码: " + code);
    }
}
