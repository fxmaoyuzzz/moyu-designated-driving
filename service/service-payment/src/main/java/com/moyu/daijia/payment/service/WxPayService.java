package com.moyu.daijia.payment.service;

import com.moyu.daijia.model.entity.payment.PaymentInfo;
import com.moyu.daijia.model.form.payment.PaymentInfoForm;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;
import jakarta.servlet.http.HttpServletRequest;

public interface WxPayService {


    /**
     * 创建微信支付
     *
     * @param paymentInfoForm
     * @return
     */
    WxPrepayVo createWxPayment(PaymentInfoForm paymentInfoForm);

    /**
     * 查询支付状态
     *
     * @param orderId
     * @return
     */
    Boolean queryPayStatus(String orderId);

    /**
     * 微信支付回调
     *
     * @param request
     */
    void wxnotify(HttpServletRequest request);

    /**
     * 支付成功后续处理
     *
     * @param orderNo
     */
    void handleOrder(String orderNo);

    /**
     * 支付时同步保存支付信息
     * @param paymentInfo
     * @return
     */
    void savePaymentInfo(PaymentInfo paymentInfo);
}
