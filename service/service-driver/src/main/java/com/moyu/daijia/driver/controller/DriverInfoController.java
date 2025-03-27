package com.moyu.daijia.driver.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.service.DriverInfoService;
import com.moyu.daijia.model.entity.driver.DriverSet;
import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "司机API接口管理")
@RestController
@RequestMapping(value = "/driver/info")
public class DriverInfoController {


    @Autowired
    private DriverInfoService driverInfoService;

    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<Long> login(@PathVariable String code) {
        log.info("调用DriverInfoController.login接口SUCCESS，入参：{}", code);
        return Result.ok(driverInfoService.login(code));
    }

    @Operation(summary = "获取司机登录信息")
    @GetMapping("/getDriverLoginInfo/{driverId}")
    public Result<DriverLoginVo> getDriverLoginInfo(@PathVariable Long driverId) {
        log.info("调用DriverInfoController.getDriverLoginInfo接口SUCCESS，入参：{}", driverId);
        DriverLoginVo driverLoginVo = driverInfoService.getDriverLoginInfo(driverId);
        return Result.ok(driverLoginVo);
    }

    @Operation(summary = "获取司机认证信息")
    @GetMapping("/getDriverAuthInfo/{driverId}")
    public Result<DriverAuthInfoVo> getDriverAuthInfo(@PathVariable Long driverId) {
        log.info("调用DriverInfoController.getDriverAuthInfo接口SUCCESS，入参：{}", driverId);

        DriverAuthInfoVo driverAuthInfoVo = driverInfoService.getDriverAuthInfo(driverId);
        return Result.ok(driverAuthInfoVo);
    }

    @Operation(summary = "更新司机认证信息")
    @PostMapping("/updateDriverAuthInfo")
    public Result<Boolean> updateDriverAuthInfo(@RequestBody UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        log.info("调用DriverInfoController.updateDriverAuthInfo接口SUCCESS，入参：{}", JSON.toJSONString(updateDriverAuthInfoForm));

        Boolean isSuccess = driverInfoService.updateDriverAuthInfo(updateDriverAuthInfoForm);
        return Result.ok(isSuccess);
    }

    @Operation(summary = "创建司机人脸模型")
    @PostMapping("/creatDriverFaceModel")
    public Result<Boolean> creatDriverFaceModel(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        log.info("调用DriverInfoController.creatDriverFaceModel接口SUCCESS，入参：{}", JSON.toJSONString(driverFaceModelForm));

        Boolean isSuccess = driverInfoService.creatDriverFaceModel(driverFaceModelForm);
        return Result.ok(isSuccess);
    }

    @Operation(summary = "获取司机设置信息")
    @GetMapping("/getDriverSet/{driverId}")
    public Result<DriverSet> getDriverSet(@PathVariable Long driverId) {
        log.info("调用DriverInfoController.getDriverSet接口SUCCESS，入参：{}", driverId);

        return Result.ok(driverInfoService.getDriverSet(driverId));
    }

    @Operation(summary = "判断司机当日是否进行过人脸识别")
    @GetMapping("/isFaceRecognition/{driverId}")
    Result<Boolean> isFaceRecognition(@PathVariable("driverId") Long driverId) {
        log.info("调用DriverInfoController.isFaceRecognition接口SUCCESS，入参：{}", driverId);

        return Result.ok(driverInfoService.isFaceRecognition(driverId));
    }

    @Operation(summary = "验证司机人脸")
    @PostMapping("/verifyDriverFace")
    public Result<Boolean> verifyDriverFace(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        log.info("调用DriverInfoController.verifyDriverFace接口SUCCESS，入参：{}", JSON.toJSONString(driverFaceModelForm));

        return Result.ok(driverInfoService.verifyDriverFace(driverFaceModelForm));
    }

    @Operation(summary = "更新司机接单状态")
    @GetMapping("/updateServiceStatus/{driverId}/{status}")
    public Result<Boolean> updateServiceStatus(@PathVariable Long driverId, @PathVariable Integer status) {
        log.info("调用DriverInfoController.updateServiceStatus接口SUCCESS，司机ID：{}，目标状态：{}", driverId, status);

        return Result.ok(driverInfoService.updateServiceStatus(driverId, status));
    }

    @Operation(summary = "获取司机基本信息")
    @GetMapping("/getDriverInfo/{driverId}")
    public Result<DriverInfoVo> getDriverInfo(@PathVariable Long driverId) {
        log.info("调用DriverInfoController.getDriverInfoOrder接口SUCCESS，司机ID：{}", driverId);

        return Result.ok(driverInfoService.getDriverInfo(driverId));
    }

    @Operation(summary = "获取司机OpenId")
    @GetMapping("/getDriverOpenId/{driverId}")
    public Result<String> getDriverOpenId(@PathVariable Long driverId) {
        log.info("调用DriverInfoController.getDriverOpenId接口SUCCESS，司机ID：{}", driverId);

        return Result.ok(driverInfoService.getDriverOpenId(driverId));
    }
}

