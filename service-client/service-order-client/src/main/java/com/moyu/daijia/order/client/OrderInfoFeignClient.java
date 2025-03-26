package com.moyu.daijia.order.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "service-order")
public interface OrderInfoFeignClient {


    /**
     * 保存订单信息
     *
     * @param orderInfoForm
     * @return
     */
    @PostMapping("/order/info/saveOrderInfo")
    Result<Long> saveOrderInfo(@RequestBody OrderInfoForm orderInfoForm);

    /**
     * 根据订单id获取订单状态
     *
     * @param orderId
     * @return
     */
    @GetMapping("/order/info/getOrderStatus/{orderId}")
    Result<Integer> getOrderStatus(@PathVariable("orderId") Long orderId);
}