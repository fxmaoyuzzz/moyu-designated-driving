package com.moyu.daijia.rules.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-rules")
public interface FeeRuleFeignClient {


    /**
     * 计算订单费用
     *
     * @param calculateOrderFeeForm
     * @return
     */
    @PostMapping("/rules/fee/calculateOrderFee")
    Result<FeeRuleResponseVo> calculateOrderFee(@RequestBody FeeRuleRequestForm calculateOrderFeeForm);
}