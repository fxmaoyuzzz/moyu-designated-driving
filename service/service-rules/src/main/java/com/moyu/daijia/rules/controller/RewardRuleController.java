package com.moyu.daijia.rules.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.rules.RewardRuleRequestForm;
import com.moyu.daijia.model.vo.rules.RewardRuleResponseVo;
import com.moyu.daijia.rules.service.RewardRuleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rules/reward")
public class RewardRuleController {

    @Autowired
    private RewardRuleService rewardRuleService;


    @Operation(summary = "计算订单奖励费用")
    @PostMapping("/calculateOrderRewardFee")
    public Result<RewardRuleResponseVo> calculateOrderRewardFee(@RequestBody RewardRuleRequestForm rewardRuleRequestForm) {
        log.info("调用FeeRuleController.calculateOrderRewardFee接口SUCCESS，入参：{}", JSON.toJSONString(rewardRuleRequestForm));
        return Result.ok(rewardRuleService.calculateOrderRewardFee(rewardRuleRequestForm));
    }
}

