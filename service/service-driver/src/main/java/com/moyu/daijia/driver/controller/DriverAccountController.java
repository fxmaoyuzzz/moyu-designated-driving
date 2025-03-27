package com.moyu.daijia.driver.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.service.DriverAccountService;
import com.moyu.daijia.model.form.driver.TransferForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "司机账户API接口管理")
@RestController
@RequestMapping(value = "/driver/account")
public class DriverAccountController {


    @Autowired
    private DriverAccountService driverAccountService;

    @Operation(summary = "转账")
    @PostMapping("/transfer")
    public Result<Boolean> transfer(@RequestBody TransferForm transferForm) {
        log.info("调用DriverAccountController.getOrderRewardFee接口SUCCESS，入参-订单号：{}", JSON.toJSONString(transferForm));

        return Result.ok(driverAccountService.transfer(transferForm));
    }
}

