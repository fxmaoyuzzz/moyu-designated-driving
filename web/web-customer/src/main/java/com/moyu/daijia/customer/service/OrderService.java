package com.moyu.daijia.customer.service;

import com.moyu.daijia.model.form.customer.ExpectOrderForm;
import com.moyu.daijia.model.form.customer.SubmitOrderForm;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.form.payment.CreateWxPaymentForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.customer.ExpectOrderVo;
import com.moyu.daijia.model.vo.driver.DriverInfoVo;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import com.moyu.daijia.model.vo.map.OrderLocationVo;
import com.moyu.daijia.model.vo.map.OrderServiceLastLocationVo;
import com.moyu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.moyu.daijia.model.vo.order.OrderInfoVo;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;

public interface OrderService {

    /**
     * 预估订单数据
     *
     * @param expectOrderForm
     * @return
     */
    ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm);

    /**
     * 乘客下单
     *
     * @param submitOrderForm
     * @return
     */
    Long submitOrder(SubmitOrderForm submitOrderForm);

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 乘客端查找当前订单
     *
     * @param customerId
     * @return
     */
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    /**
     * 获取订单信息
     *
     * @param orderId
     * @param customerId
     * @return
     */
    OrderInfoVo getOrderInfo(Long orderId, Long customerId);

    /**
     * 根据订单id获取司机基本信息
     *
     * @param orderId
     * @param customerId
     * @return
     */
    DriverInfoVo getDriverInfo(Long orderId, Long customerId);

    /**
     * 司机赶往代驾起始点：获取订单经纬度位置
     *
     * @param orderId
     * @return
     */
    OrderLocationVo getCacheOrderLocation(Long orderId);

    /**
     * 计算最佳驾驶线路
     *
     * @param calculateDrivingLineForm
     * @return
     */
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);

    /**
     * 获取订单服务最后一个位置信息
     *
     * @param orderId
     * @return
     */
    OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId);

    /**
     * 获取乘客订单分页列表
     *
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    PageVo findCustomerOrderPage(Long customerId, Long page, Long limit);

    /**
     * 创建微信支付
     *
     * @param createWxPaymentForm
     * @return
     */
    WxPrepayVo createWxPayment(CreateWxPaymentForm createWxPaymentForm);

    /**
     * 支付状态查询
     *
     * @param orderNo
     * @return
     */
    Boolean queryPayStatus(String orderNo);
}
