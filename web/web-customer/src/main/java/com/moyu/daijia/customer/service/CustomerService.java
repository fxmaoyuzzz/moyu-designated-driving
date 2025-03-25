package com.moyu.daijia.customer.service;

import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;

public interface CustomerService {


    /**
     * 用户登录
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 获取登录用户信息
     * @param token
     * @return
     */
    CustomerLoginVo getCustomerLoginInfo(String token);

    /**
     * 更新微信手机号码
     * @param updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);

}
