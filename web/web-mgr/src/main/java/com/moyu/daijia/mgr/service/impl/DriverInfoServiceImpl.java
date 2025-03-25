package com.moyu.daijia.mgr.service.impl;

import com.moyu.daijia.driver.client.DriverInfoFeignClient;
import com.moyu.daijia.mgr.service.DriverInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoServiceImpl implements DriverInfoService {

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;



}