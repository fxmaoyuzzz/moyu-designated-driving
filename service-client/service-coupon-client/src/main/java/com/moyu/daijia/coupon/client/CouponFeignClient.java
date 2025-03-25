package com.moyu.daijia.coupon.client;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "service-coupon")
public interface CouponFeignClient {


}