package com.moyu.daijia.order.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.order.OrderMonitor;
import com.moyu.daijia.model.entity.order.OrderMonitorRecord;
import com.moyu.daijia.order.service.OrderMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order/monitor")
public class OrderMonitorController {

    @Autowired
    private OrderMonitorService orderMonitorService;

    @Operation(summary = "保存订单监控记录数据")
    @PostMapping("/saveOrderMonitorRecord")
    public Result<Boolean> saveMonitorRecord(@RequestBody OrderMonitorRecord orderMonitorRecord) {
        log.info("调用OrderMonitorController.saveMonitorRecord接口SUCCESS，入参：{}", JSON.toJSONString(orderMonitorService));

        return Result.ok(orderMonitorService.saveOrderMonitorRecord(orderMonitorRecord));
    }

    @Operation(summary = "根据订单id获取订单监控信息")
    @GetMapping("/getOrderMonitor/{orderId}")
    public Result<OrderMonitor> getOrderMonitor(@PathVariable Long orderId) {
        return Result.ok(orderMonitorService.getOrderMonitor(orderId));
    }

    @Operation(summary = "更新订单监控信息")
    @PostMapping("/updateOrderMonitor")
    public Result<Boolean> updateOrderMonitor(@RequestBody OrderMonitor OrderMonitor) {
        return Result.ok(orderMonitorService.updateOrderMonitor(OrderMonitor));
    }
}

