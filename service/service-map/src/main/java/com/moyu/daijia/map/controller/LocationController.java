package com.moyu.daijia.map.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.map.service.LocationService;
import com.moyu.daijia.model.form.map.OrderServiceLocationForm;
import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.form.map.UpdateOrderLocationForm;
import com.moyu.daijia.model.vo.map.NearByDriverVo;
import com.moyu.daijia.model.vo.map.OrderLocationVo;
import com.moyu.daijia.model.vo.map.OrderServiceLastLocationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Tag(name = "位置API接口管理")
@RestController
@RequestMapping("/map/location")
public class LocationController {


    @Autowired
    private LocationService locationService;

    @Operation(summary = "开启接单服务：更新司机经纬度位置")
    @PostMapping("/updateDriverLocation")
    public Result<Boolean> updateDriverLocation(@RequestBody
                                                UpdateDriverLocationForm updateDriverLocationForm) {
        log.info("调用LocationController.updateDriverLocation接口SUCCESS，入参：{}", JSON.toJSONString(updateDriverLocationForm));

        Boolean flag = locationService.updateDriverLocation(updateDriverLocationForm);
        return Result.ok(flag);
    }

    @Operation(summary = "关闭接单服务：删除司机经纬度位置")
    @DeleteMapping("/removeDriverLocation/{driverId}")
    public Result<Boolean> removeDriverLocation(@PathVariable Long driverId) {
        log.info("调用LocationController.removeDriverLocation接口SUCCESS，入参：{}", JSON.toJSONString(driverId));

        return Result.ok(locationService.removeDriverLocation(driverId));
    }

    @Operation(summary = "搜索附近满足条件的司机")
    @PostMapping("/searchNearByDriver")
    public Result<List<NearByDriverVo>> searchNearByDriver(@RequestBody
                                                           SearchNearByDriverForm searchNearByDriverForm) {
        log.info("调用LocationController.searchNearByDriver接口SUCCESS，入参：{}", JSON.toJSONString(searchNearByDriverForm));

        return Result.ok(locationService.searchNearByDriver(searchNearByDriverForm));
    }

    @Operation(summary = "司机赶往代驾起始点：更新订单地址到缓存")
    @PostMapping("/updateOrderLocationToCache")
    public Result<Boolean> updateOrderLocationToCache(@RequestBody UpdateOrderLocationForm updateOrderLocationForm) {
        log.info("调用LocationController.updateOrderLocationToCache接口SUCCESS，入参：{}", JSON.toJSONString(updateOrderLocationForm));

        return Result.ok(locationService.updateOrderLocationToCache(updateOrderLocationForm));
    }

    @Operation(summary = "司机赶往代驾起始点：获取订单经纬度位置")
    @GetMapping("/getCacheOrderLocation/{orderId}")
    public Result<OrderLocationVo> getCacheOrderLocation(@PathVariable Long orderId) {
        log.info("调用LocationController.getCacheOrderLocation接口SUCCESS，入参：{}", orderId);

        return Result.ok(locationService.getCacheOrderLocation(orderId));
    }

    @Operation(summary = "批量保存代驾服务订单位置")
    @PostMapping("/saveOrderServiceLocation")
    public Result<Boolean> saveOrderServiceLocation(@RequestBody List<OrderServiceLocationForm> orderLocationServiceFormList) {
        log.info("调用LocationController.updateOrderLocationToCache接口SUCCESS，入参：{}", JSON.toJSONString(orderLocationServiceFormList));

        return Result.ok(locationService.saveOrderServiceLocation(orderLocationServiceFormList));
    }

    @Operation(summary = "代驾服务：获取订单服务最后一个位置信息")
    @GetMapping("/getOrderServiceLastLocation/{orderId}")
    public Result<OrderServiceLastLocationVo> getOrderServiceLastLocation(@PathVariable Long orderId) {
        log.info("调用LocationController.getOrderServiceLastLocation接口SUCCESS，入参：{}", orderId);

        return Result.ok(locationService.getOrderServiceLastLocation(orderId));
    }

    @Operation(summary = "计算订单实际里程")
    @GetMapping("/calculateOrderRealDistance/{orderId}")
    public Result<BigDecimal> calculateOrderRealDistance(@PathVariable Long orderId) {
        log.info("调用LocationController.calculateOrderRealDistance接口SUCCESS，入参：{}", orderId);

        return Result.ok(locationService.calculateOrderRealDistance(orderId));
    }
}

