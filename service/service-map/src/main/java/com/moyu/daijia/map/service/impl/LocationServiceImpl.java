package com.moyu.daijia.map.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.constant.SystemConstant;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.client.DriverInfoFeignClient;
import com.moyu.daijia.map.service.LocationService;
import com.moyu.daijia.model.entity.driver.DriverSet;
import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.vo.map.NearByDriverVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Override
    public Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm) {
        Point point = new Point(updateDriverLocationForm.getLongitude().doubleValue(),
                updateDriverLocationForm.getLatitude().doubleValue());
        redisTemplate.opsForGeo().add(RedisConstant.DRIVER_GEO_LOCATION,
                point,
                updateDriverLocationForm.getDriverId().toString());
        return true;
    }

    @Override
    public Boolean removeDriverLocation(Long driverId) {
        redisTemplate.opsForGeo().remove(RedisConstant.DRIVER_GEO_LOCATION, driverId.toString());
        return true;
    }

    @Override
    public List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm) {
        // 搜索经纬度位置5公里以内的司机
        Point point = new Point(searchNearByDriverForm.getLongitude().doubleValue(),
                searchNearByDriverForm.getLatitude().doubleValue());

        // 定义距离，5公里
        Distance distance = new Distance(SystemConstant.NEARBY_DRIVER_RADIUS,
                RedisGeoCommands.DistanceUnit.KILOMETERS);

        // 创建circle对象，point  distance
        Circle circle = new Circle(point, distance);

        // 定义GEO参数，设置返回结果包含内容
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()  // 包含距离
                        .includeCoordinates() // 包含坐标
                        .sortAscending(); // 升序

        GeoResults<RedisGeoCommands.GeoLocation<String>> result =
                redisTemplate.opsForGeo().radius(RedisConstant.DRIVER_GEO_LOCATION, circle, args);
        log.info("获取司机位置缓存结果：{}", JSON.toJSONString(result));

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = result.getContent();

        // 根据每个司机个性化设置信息判断
        List<NearByDriverVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(content)) {
            Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator = content.iterator();
            while (iterator.hasNext()) {
                GeoResult<RedisGeoCommands.GeoLocation<String>> item = iterator.next();

                Long driverId = Long.parseLong(item.getContent().getName());

                // 远程调用，根据司机id个性化设置信息
                Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
                log.info("调用远程service-driver服务getDriverSet结果：{}", JSON.toJSONString(driverSetResult));

                DriverSet driverSet = driverSetResult.getData();

                // 判断订单里程order_distance
                BigDecimal orderDistance = driverSet.getOrderDistance();
                // orderDistance==0，司机没有限制的
                // 如果不等于0 ，比如30，接单30公里代驾订单。
                // 接单距离 - 当前单子距离  < 0,不复合条件
                // 30          35
                if (orderDistance.doubleValue() != 0
                        && orderDistance.subtract(searchNearByDriverForm.getMileageDistance()).doubleValue() < 0) {
                    continue;
                }

                // 判断接单里程 accept_distance
                // 当前接单距离
                BigDecimal currentDistance =
                        new BigDecimal(item.getDistance().getValue()).setScale(2, RoundingMode.HALF_UP);

                BigDecimal acceptDistance = driverSet.getAcceptDistance();
                if (acceptDistance.doubleValue() != 0
                        && acceptDistance.subtract(currentDistance).doubleValue() < 0) {
                    continue;
                }

                NearByDriverVo nearByDriverVo = new NearByDriverVo();
                nearByDriverVo.setDriverId(driverId);
                nearByDriverVo.setDistance(currentDistance);
                list.add(nearByDriverVo);

            }
        }
        return list;
    }
}
