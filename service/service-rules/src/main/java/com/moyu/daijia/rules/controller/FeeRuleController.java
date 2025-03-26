package com.moyu.daijia.rules.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.moyu.daijia.rules.service.FeeRuleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rules/fee")
public class FeeRuleController {


    @Autowired
    private FeeRuleService feeRuleService;

    @Operation(summary = "计算订单费用")
    @PostMapping("/calculateOrderFee")
    public Result<FeeRuleResponseVo> calculateOrderFee(@RequestBody FeeRuleRequestForm calculateOrderFeeForm) {
        log.info("调用FeeRuleController.calculateOrderFee接口SUCCESS，入参：{}", JSON.toJSONString(calculateOrderFeeForm));
        FeeRuleResponseVo feeRuleResponseVo = feeRuleService.calculateOrderFee(calculateOrderFeeForm);
        return Result.ok(feeRuleResponseVo);
    }
}

