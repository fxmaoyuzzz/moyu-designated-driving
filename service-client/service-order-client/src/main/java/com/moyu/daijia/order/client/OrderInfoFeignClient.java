package com.moyu.daijia.order.client;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "service-order")
public interface OrderInfoFeignClient {


}