package com.moyu.daijia.customer.controller;

import com.moyu.daijia.common.login.GlobalLogin;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.customer.service.OrderService;
import com.moyu.daijia.model.form.customer.ExpectOrderForm;
import com.moyu.daijia.model.form.customer.SubmitOrderForm;
import com.moyu.daijia.model.vo.customer.ExpectOrderVo;
import com.moyu.daijia.model.vo.order.CurrentOrderInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "订单API接口管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "查找乘客端当前订单")
    @GlobalLogin
    @GetMapping("/searchCustomerCurrentOrder")
    public Result<CurrentOrderInfoVo> searchCustomerCurrentOrder() {
        CurrentOrderInfoVo currentOrderInfoVo = new CurrentOrderInfoVo();
        currentOrderInfoVo.setIsHasCurrentOrder(false);
        return Result.ok(currentOrderInfoVo);
    }


    @Operation(summary = "预估订单数据")
    @GlobalLogin
    @PostMapping("/expectOrder")
    public Result<ExpectOrderVo> expectOrder(@RequestBody ExpectOrderForm expectOrderForm) {
        return Result.ok(orderService.expectOrder(expectOrderForm));
    }

    @Operation(summary = "乘客下单")
    @GlobalLogin
    @PostMapping("/submitOrder")
    public Result<Long> submitOrder(@RequestBody SubmitOrderForm submitOrderForm) {
        submitOrderForm.setCustomerId(AuthContextHolder.getUserId());
        return Result.ok(orderService.submitOrder(submitOrderForm));
    }

    @Operation(summary = "查询订单状态")
    @GlobalLogin
    @GetMapping("/getOrderStatus/{orderId}")
    public Result<Integer> getOrderStatus(@PathVariable Long orderId) {
        return Result.ok(orderService.getOrderStatus(orderId));
    }
}

