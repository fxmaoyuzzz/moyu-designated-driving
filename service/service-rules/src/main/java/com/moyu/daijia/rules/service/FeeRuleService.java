package com.moyu.daijia.rules.service;

import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;

public interface FeeRuleService {

    /**
     * 计算订单费用
     *
     * @param calculateOrderFeeForm
     * @return
     */
    FeeRuleResponseVo calculateOrderFee(FeeRuleRequestForm calculateOrderFeeForm);
}
