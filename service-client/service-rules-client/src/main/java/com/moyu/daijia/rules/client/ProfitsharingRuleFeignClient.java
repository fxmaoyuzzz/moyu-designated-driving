package com.moyu.daijia.rules.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-rules")
public interface ProfitsharingRuleFeignClient {


    /**
     * 计算订单分账数据
     *
     * @param profitsharingRuleRequestForm
     * @return
     */
    @PostMapping("/rules/profitsharing/calculateOrderProfitsharingFee")
    Result<ProfitsharingRuleResponseVo> calculateOrderProfitsharingFee(@RequestBody ProfitsharingRuleRequestForm profitsharingRuleRequestForm);
}