package com.moyu.daijia.model.form.system;

import lombok.Data;

@Data
public class LoginForm {

    /**
     * 手机号
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
