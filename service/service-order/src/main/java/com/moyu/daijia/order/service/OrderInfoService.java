package com.moyu.daijia.order.service;

import com.moyu.daijia.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.daijia.model.form.order.OrderInfoForm;

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
}
