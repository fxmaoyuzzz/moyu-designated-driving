package com.moyu.daijia.customer.controller;

import com.moyu.daijia.common.login.GlobalLogin;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.customer.service.CustomerService;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "客户API接口管理")
@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerInfoService;

    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<String> wxLogin(@PathVariable String code) {
        return Result.ok(customerInfoService.login(code));
    }

    @GlobalLogin
    @Operation(summary = "获取客户登录信息")
    @GetMapping("/getCustomerLoginInfo")
    public Result<CustomerLoginVo>
    getCustomerLoginInfo(@RequestHeader(value = "token") String token) {
        CustomerLoginVo customerLoginVo = customerInfoService.getCustomerLoginInfo(token);

        return Result.ok(customerLoginVo);
    }

    @Operation(summary = "更新用户微信手机号")
    @GlobalLogin
    @PostMapping("/updateWxPhone")
    public Result updateWxPhone(@RequestBody UpdateWxPhoneForm updateWxPhoneForm) {
        updateWxPhoneForm.setCustomerId(AuthContextHolder.getUserId());
        return Result.ok(customerInfoService.updateWxPhoneNumber(updateWxPhoneForm));
    }
}

