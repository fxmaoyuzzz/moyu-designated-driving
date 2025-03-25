package com.moyu.daijia.mgr.service.impl;

import com.moyu.daijia.mgr.service.OrderInfoService;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoServiceImpl implements OrderInfoService {

	@Autowired
	private OrderInfoFeignClient orderInfoFeignClient;



}
