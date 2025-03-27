package com.moyu.daijia.map.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.payment.PaymentInfoForm;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "service-payment")
public interface WxPayFeignClient {

    /**
     * 创建微信支付
     *
     * @param paymentInfoForm
     * @return
     */
    @PostMapping("/payment/wxPay/createWxPayment")
    Result<WxPrepayVo> createWxPayment(@RequestBody PaymentInfoForm paymentInfoForm);

    /**
     * 支付状态查询
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/payment/wxPay/queryPayStatus/{orderNo}")
    Result<Boolean> queryPayStatus(@PathVariable("orderNo") String orderNo);
}