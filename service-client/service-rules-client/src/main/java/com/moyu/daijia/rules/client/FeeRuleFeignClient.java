package com.moyu.daijia.rules.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "service-rules")
public interface FeeRuleFeignClient {


}