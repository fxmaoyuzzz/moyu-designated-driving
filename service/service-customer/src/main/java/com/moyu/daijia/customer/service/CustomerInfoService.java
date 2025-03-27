package com.moyu.daijia.customer.service;

import com.moyu.daijia.model.entity.customer.CustomerInfo;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    /**
     * 小程序授权登录
     * @param code
     * @return
     */
    Long login(String code);

    /**
     * 获取客户登录信息
     * @param customerId
     * @return
     */
    CustomerLoginVo getCustomerInfo(Long customerId);

    /**
     * 更新客户微信手机号码
     * @param updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);

    /**
     * 获取客户OpenId
     * @param customerId
     * @return
     */
    String getCustomerOpenId(Long customerId);
}
