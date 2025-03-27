package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.driver.client.DriverInfoFeignClient;
import com.moyu.daijia.driver.service.LocationService;
import com.moyu.daijia.map.client.LocationFeignClient;
import com.moyu.daijia.model.entity.driver.DriverSet;
import com.moyu.daijia.model.form.map.OrderServiceLocationForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.form.map.UpdateOrderLocationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {


    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Override
    public Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm) {
        // 根据司机id获取司机个性化设置信息
        Long driverId = updateDriverLocationForm.getDriverId();
        Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
        log.info("调用远程service-driver服务getDriverSet结果：{}", JSON.toJSONString(driverSetResult));


        DriverSet driverSet = driverSetResult.getData();

        // 判断：如果司机开始接单，更新位置信息
        if (driverSet.getServiceStatus().equals(ResultCodeEnum.DRIVER_SERVICE_OPEN.getCode())) {
            Result<Boolean> booleanResult = locationFeignClient.updateDriverLocation(updateDriverLocationForm);
            log.info("调用远程service-map服务updateDriverLocation结果：{}", JSON.toJSONString(booleanResult));

            return booleanResult.getData();
        } else {
            log.warn("更新司机位置信息失败，司机未开启接单服务");
            throw new MoyuException(ResultCodeEnum.NO_START_SERVICE);
        }
    }

    @Override
    public Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm) {
        Boolean data = locationFeignClient.updateOrderLocationToCache(updateOrderLocationForm).getData();
        log.info("调用远程service-map服务updateOrderLocationToCache结果：{}", data);

        return data;
    }

    @Override
    public Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList) {
        Boolean data = locationFeignClient.saveOrderServiceLocation(orderLocationServiceFormList).getData();
        log.info("调用远程service-map服务saveOrderServiceLocation结果：{}", data);

        return data;
    }
}
