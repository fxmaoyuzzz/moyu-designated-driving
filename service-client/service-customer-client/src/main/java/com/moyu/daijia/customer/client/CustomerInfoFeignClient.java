package com.moyu.daijia.customer.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-customer")
public interface CustomerInfoFeignClient {


    /**
     * 小程序授权登录接口
     *
     * @param code
     * @return
     */
    @GetMapping("/customer/info/login/{code}")
    Result<Long> login(@PathVariable String code);

    /**
     * 获取用户登录信息
     *
     * @param customerId
     * @return
     */
    @GetMapping("/customer/info/getCustomerLoginInfo/{customerId}")
    Result<CustomerLoginVo> getCustomerLoginInfo(@PathVariable Long customerId);

    /**
     * 更新微信手机号码
     * @param updateWxPhoneForm
     * @return
     */
    @PostMapping("/customer/info/updateWxPhoneNumber")
    Result<Boolean> updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}