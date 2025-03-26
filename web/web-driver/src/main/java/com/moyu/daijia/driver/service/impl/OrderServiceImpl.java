package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.dispatch.client.NewOrderFeignClient;
import com.moyu.daijia.driver.service.OrderService;
import com.moyu.daijia.model.vo.order.NewOrderDataVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;

    @Override
    public Integer getOrderStatus(Long orderId) {
        Integer result = orderInfoFeignClient.getOrderStatus(orderId).getData();
        log.info("调用远程service-order服务getOrderStatus结果：{}", JSON.toJSONString(result));

        return result;
    }

    @Override
    public List<NewOrderDataVo> findNewOrderQueueData(Long driverId) {
        List<NewOrderDataVo> list = newOrderFeignClient.findNewOrderQueueData(driverId).getData();
        log.info("调用远程service-dispatch服务findNewOrderQueueData结果数量：{}", list.size());

        return list;
    }
}
