package com.moyu.daijia.dispatch.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.dispatch.service.NewOrderService;
import com.moyu.daijia.model.vo.dispatch.NewOrderTaskVo;
import com.moyu.daijia.model.vo.order.NewOrderDataVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "司机新订单接口管理")
@RestController
@RequestMapping("/dispatch/newOrder")
public class NewOrderController {


    @Autowired
    private NewOrderService newOrderService;

    /**
     * 创建并启动任务调度方法
     *
     * @param newOrderTaskVo
     * @return
     */
    @Operation(summary = "添加并开始新订单任务调度")
    @PostMapping("/addAndStartTask")
    public Result<Long> addAndStartTask(@RequestBody NewOrderTaskVo newOrderTaskVo) {
        Long id = newOrderService.addAndStartTask(newOrderTaskVo);
        return Result.ok(id);
    }

    @Operation(summary = "查询司机新订单数据")
    @GetMapping("/findNewOrderQueueData/{driverId}")
    public Result<List<NewOrderDataVo>> findNewOrderQueueData(@PathVariable Long driverId) {
        return Result.ok(newOrderService.findNewOrderQueueData(driverId));
    }

    @Operation(summary = "清空新订单队列数据")
    @GetMapping("/clearNewOrderQueueData/{driverId}")
    public Result<Boolean> clearNewOrderQueueData(@PathVariable Long driverId) {
        return Result.ok(newOrderService.clearNewOrderQueueData(driverId));
    }
}

