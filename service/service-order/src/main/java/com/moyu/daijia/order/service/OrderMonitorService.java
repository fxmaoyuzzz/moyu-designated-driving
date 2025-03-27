package com.moyu.daijia.order.service;

import com.moyu.daijia.model.entity.order.OrderMonitor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.daijia.model.entity.order.OrderMonitorRecord;

public interface OrderMonitorService extends IService<OrderMonitor> {

    /**
     * 保存订单监控记录数据
     *
     * @param orderMonitorRecord
     * @return
     */
    Boolean saveOrderMonitorRecord(OrderMonitorRecord orderMonitorRecord);

    /**
     * 根据订单id获取订单监控信息
     *
     * @param orderId
     * @return
     */
    OrderMonitor getOrderMonitor(Long orderId);

    /**
     * 更新订单监控信息
     *
     * @param orderMonitor
     * @return
     */
    Boolean updateOrderMonitor(OrderMonitor orderMonitor);

    /**
     * 新增订单监控信息
     *
     * @param orderMonitor
     */
    void saveOrderMonitor(OrderMonitor orderMonitor);
}
