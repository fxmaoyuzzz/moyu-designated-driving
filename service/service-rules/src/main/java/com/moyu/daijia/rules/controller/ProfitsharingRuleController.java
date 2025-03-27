package com.moyu.daijia.rules.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import com.moyu.daijia.rules.service.ProfitsharingRuleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rules/profitsharing")
public class ProfitsharingRuleController {

    @Autowired
    private ProfitsharingRuleService profitsharingRuleService;

    @Operation(summary = "计算系统分账费用")
    @PostMapping("/calculateOrderProfitsharingFee")
    public Result<ProfitsharingRuleResponseVo> calculateOrderProfitsharingFee(@RequestBody ProfitsharingRuleRequestForm profitsharingRuleRequestForm) {
        log.info("调用FeeRuleController.calculateOrderProfitsharingFee接口SUCCESS，入参：{}", JSON.toJSONString(profitsharingRuleRequestForm));

        return Result.ok(profitsharingRuleService.calculateOrderProfitsharingFee(profitsharingRuleRequestForm));
    }
}

