package com.moyu.daijia.order.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.order.OrderMonitor;
import com.moyu.daijia.model.entity.order.OrderMonitorRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "service-order")
public interface OrderMonitorFeignClient {


    /**
     * 保存订单监控记录数据
     *
     * @param orderMonitorRecord
     * @return
     */
    @PostMapping("/order/monitor/saveOrderMonitorRecord")
    Result<Boolean> saveMonitorRecord(@RequestBody OrderMonitorRecord orderMonitorRecord);

    /**
     * 根据订单id获取订单监控信息
     */
    @GetMapping("/order/monitor/getOrderMonitor/{orderId}")
    Result<OrderMonitor> getOrderMonitor(@PathVariable Long orderId);

    /**
     * 更新订单监控信息
     *
     * @param OrderMonitor
     * @return
     */
    @PostMapping("/order/monitor/updateOrderMonitor")
    Result<Boolean> updateOrderMonitor(@RequestBody OrderMonitor OrderMonitor);
}