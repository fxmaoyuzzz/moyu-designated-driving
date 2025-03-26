package com.moyu.daijia.customer.service;

import com.moyu.daijia.model.form.customer.ExpectOrderForm;
import com.moyu.daijia.model.form.customer.SubmitOrderForm;
import com.moyu.daijia.model.vo.customer.ExpectOrderVo;

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
}
