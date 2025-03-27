package com.moyu.daijia.customer.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.customer.service.CustomerInfoService;
import com.moyu.daijia.model.entity.customer.CustomerInfo;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 */
@Slf4j
@RestController
@RequestMapping("/customer/info")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoService customerInfoService;

    @Operation(summary = "获取客户基本信息")
    @GetMapping("/getCustomerInfo/{customerId}")
    public Result<CustomerInfo> getCustomerInfo(@PathVariable Long customerId) {
        log.info("调用CustomerInfoController.getCustomerInfo接口SUCCESS，入参：{}", customerId);
        return Result.ok(customerInfoService.getById(customerId));
    }

    /**
     * 微信小程序登录接口
     */
    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<Long> login(@PathVariable String code) {
        log.info("调用CustomerInfoController.login接口SUCCESS，入参：{}", code);
        return Result.ok(customerInfoService.login(code));
    }

    /**
     * 获取客户登录信息
     * @param customerId
     * @return
     */
    @Operation(summary = "获取客户登录信息")
    @GetMapping("/getCustomerLoginInfo/{customerId}")
    public Result<CustomerLoginVo> getCustomerLoginInfo(@PathVariable Long customerId) {
        log.info("调用CustomerInfoController.getCustomerLoginInfo接口SUCCESS，入参：{}", customerId);
        CustomerLoginVo customerLoginVo = customerInfoService.getCustomerInfo(customerId);
        return Result.ok(customerLoginVo);
    }

    /**
     * 更新客户微信手机号码
     * @param updateWxPhoneForm
     * @return
     */
    @Operation(summary = "更新客户微信手机号码")
    @PostMapping("/updateWxPhoneNumber")
    public Result<Boolean> updateWxPhoneNumber(@RequestBody UpdateWxPhoneForm updateWxPhoneForm) {
        log.info("调用CustomerInfoController.updateWxPhoneNumber接口SUCCESS，入参：{}", JSON.toJSONString(updateWxPhoneForm));
        return Result.ok(customerInfoService.updateWxPhoneNumber(updateWxPhoneForm));
    }

    @Operation(summary = "获取客户OpenId")
    @GetMapping("/getCustomerOpenId/{customerId}")
    public Result<String> getCustomerOpenId(@PathVariable Long customerId) {
        log.info("调用CustomerInfoController.getCustomerOpenId接口SUCCESS，入参：{}", customerId);
        return Result.ok(customerInfoService.getCustomerOpenId(customerId));
    }
}

