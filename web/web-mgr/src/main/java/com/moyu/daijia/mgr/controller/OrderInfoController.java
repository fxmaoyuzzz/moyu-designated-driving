package com.moyu.daijia.mgr.controller;

import com.moyu.daijia.mgr.service.OrderInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "位置API接口管理")
@RestController
@RequestMapping(value="/order/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoController {
	
	@Autowired
	private OrderInfoService orderInfoService;


}

