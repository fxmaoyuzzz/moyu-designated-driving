package com.moyu.daijia.order.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "订单API接口管理")
@RestController
@RequestMapping(value = "/order/info")
public class OrderInfoController {


    @Autowired
    private OrderInfoService orderInfoService;

    @Operation(summary = "保存订单信息")
    @PostMapping("/saveOrderInfo")
    public Result<Long> saveOrderInfo(@RequestBody OrderInfoForm orderInfoForm) {
        log.info("调用OrderInfoController.saveOrderInfo接口SUCCESS，入参：{}", JSON.toJSONString(orderInfoForm));

        return Result.ok(orderInfoService.saveOrderInfo(orderInfoForm));
    }

    @Operation(summary = "根据订单id获取订单状态")
    @GetMapping("/getOrderStatus/{orderId}")
    public Result<Integer> getOrderStatus(@PathVariable Long orderId) {
        return Result.ok(orderInfoService.getOrderStatus(orderId));
    }
}

