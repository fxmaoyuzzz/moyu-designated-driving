package com.moyu.daijia.map.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.map.service.LocationService;
import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.vo.map.NearByDriverVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

