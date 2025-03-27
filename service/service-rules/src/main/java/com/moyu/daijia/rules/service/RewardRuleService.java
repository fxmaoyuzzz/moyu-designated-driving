package com.moyu.daijia.rules.service;

import com.moyu.daijia.model.form.rules.RewardRuleRequestForm;
import com.moyu.daijia.model.vo.rules.RewardRuleResponseVo;

public interface RewardRuleService {

    /**
     * 计算订单奖励费用
     *
     * @param rewardRuleRequestForm
     * @return
     */
    RewardRuleResponseVo calculateOrderRewardFee(RewardRuleRequestForm rewardRuleRequestForm);
}
