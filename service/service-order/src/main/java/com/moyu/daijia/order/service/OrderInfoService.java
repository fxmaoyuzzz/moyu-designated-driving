package com.moyu.daijia.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.model.form.order.StartDriveForm;
import com.moyu.daijia.model.form.order.UpdateOrderBillForm;
import com.moyu.daijia.model.form.order.UpdateOrderCartForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.order.*;

import java.math.BigDecimal;

public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 保存订单信息
     *
     * @param orderInfoForm
     * @return
     */
    Long saveOrderInfo(OrderInfoForm orderInfoForm);

    /**
     * 根据订单id获取订单状态
     *
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 司机抢单
     *
     * @param driverId
     * @param orderId
     * @return
     */
    Boolean robNewOrder(Long driverId, Long orderId);

    /**
     * 乘客端查找当前订单
     *
     * @param customerId
     * @return
     */
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    /**
     * 司机端查找当前订单
     *
     * @param driverId
     * @return
     */
    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    /**
     * 司机到达起始点
     *
     * @param orderId
     * @param driverId
     * @return
     */
    Boolean driverArriveStartLocation(Long orderId, Long driverId);

    /**
     * 更新代驾车辆信息
     *
     * @param updateOrderCartForm
     * @return
     */
    Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm);

    /**
     * 开启代驾服务
     *
     * @param startDriveForm
     * @return
     */
    Boolean startDriver(StartDriveForm startDriveForm);


    /**
     * 根据时间段获取订单数据量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Long getOrderNumByTime(String startTime, String endTime);

    /**
     * 结束代驾服务更新订单账单
     *
     * @param updateOrderBillForm
     * @return
     */
    Boolean endDrive(UpdateOrderBillForm updateOrderBillForm);

    /**
     * 获取乘客订单分页列表
     *
     * @param pageParam
     * @param customerId
     * @return
     */
    PageVo findCustomerOrderPage(Page<OrderInfo> pageParam, Long customerId);

    /**
     * 获取司机订单分页列表
     *
     * @param pageParam
     * @param driverId
     * @return
     */
    PageVo findDriverOrderPage(Page<OrderInfo> pageParam, Long driverId);

    /**
     * 根据订单id获取实际账单信息
     *
     * @param orderId
     * @return
     */
    OrderBillVo getOrderBillInfo(Long orderId);

    /**
     * 根据订单id获取实际分账信息
     *
     * @param orderId
     * @return
     */
    OrderProfitsharingVo getOrderProfitsharing(Long orderId);

    /**
     * 发送账单信息
     *
     * @param orderId
     * @param driverId
     * @return
     */
    Boolean sendOrderBillInfo(Long orderId, Long driverId);

    /**
     * 获取订单支付信息
     *
     * @param orderNo
     * @param customerId
     * @return
     */
    OrderPayVo getOrderPayVo(String orderNo, Long customerId);

    /**
     * 更改订单支付状态
     *
     * @param orderNo
     * @return
     */
    Boolean updateOrderPayStatus(String orderNo);

    /**
     * 获取订单的系统奖励
     *
     * @param orderNo
     * @return
     */
    OrderRewardVo getOrderRewardFee(String orderNo);

    /**
     * 调用方法取消订单
     *
     * @param parseLong
     */
    void orderCancel(long parseLong);

    /**
     * 更新订单优惠券金额
     *
     * @param orderId
     * @param couponAmount
     * @return
     */
    Boolean updateCouponAmount(Long orderId, BigDecimal couponAmount);

    /**
     * 更新分账状态
     *
     * @param orderNo
     */
    void updateProfitsharingStatus(String orderNo);

    /**
     *
     * 根据订单号查询订单详情
     * @param orderNo
     * @return
     */
    OrderInfo getByOrderNo(String orderNo);
}
