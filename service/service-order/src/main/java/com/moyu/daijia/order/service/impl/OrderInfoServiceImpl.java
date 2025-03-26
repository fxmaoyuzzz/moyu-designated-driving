package com.moyu.daijia.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.moyu.daijia.model.entity.order.OrderStatusLog;
import com.moyu.daijia.model.enums.OrderStatus;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.order.mapper.OrderInfoMapper;
import com.moyu.daijia.order.mapper.OrderStatusLogMapper;
import com.moyu.daijia.order.service.OrderInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Override
    public Long saveOrderInfo(OrderInfoForm orderInfoForm) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoForm, orderInfo);

        // TODO: 2024/6/26 后期使用雪花算法生成订单号

        // 订单号
        String orderNo = IdUtil.simpleUUID();
        orderInfo.setOrderNo(orderNo);
        // 订单状态
        orderInfo.setStatus(OrderStatus.WAITING_ACCEPT.getStatus());
        orderInfoMapper.insert(orderInfo);

        // 记录日志
        this.log(orderInfo.getId(), orderInfo.getStatus());

        return orderInfo.getId();
    }

    @Override
    public Integer getOrderStatus(Long orderId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId, orderId);
        wrapper.select(OrderInfo::getStatus);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        if (orderInfo == null) {
            return OrderStatus.NULL_ORDER.getStatus();
        }

        return orderInfo.getStatus();
    }

    public void log(Long orderId, Integer status) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        orderStatusLog.setOrderStatus(status);
        orderStatusLog.setOperateTime(new Date());
        orderStatusLogMapper.insert(orderStatusLog);
    }
}
