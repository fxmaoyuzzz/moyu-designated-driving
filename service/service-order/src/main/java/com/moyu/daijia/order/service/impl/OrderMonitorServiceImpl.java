package com.moyu.daijia.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.model.entity.order.OrderMonitor;
import com.moyu.daijia.model.entity.order.OrderMonitorRecord;
import com.moyu.daijia.order.mapper.OrderMonitorMapper;
import com.moyu.daijia.order.repository.OrderMonitorRecordRepository;
import com.moyu.daijia.order.service.OrderMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMonitorServiceImpl extends ServiceImpl<OrderMonitorMapper, OrderMonitor> implements OrderMonitorService {


    @Autowired
    private OrderMonitorRecordRepository orderMonitorRecordRepository;


    @Override
    public Boolean saveOrderMonitorRecord(OrderMonitorRecord orderMonitorRecord) {
        orderMonitorRecordRepository.save(orderMonitorRecord);
        return true;
    }

    @Override
    public OrderMonitor getOrderMonitor(Long orderId) {
        return this.getOne(new LambdaQueryWrapper<OrderMonitor>().eq(OrderMonitor::getOrderId, orderId));
    }

    @Override
    public Boolean updateOrderMonitor(OrderMonitor orderMonitor) {
        return this.updateById(orderMonitor);
    }

    @Override
    public void saveOrderMonitor(OrderMonitor orderMonitor) {
        this.save(orderMonitor);
    }
}
