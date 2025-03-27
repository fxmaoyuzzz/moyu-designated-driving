package com.moyu.daijia.rules.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.model.form.rules.RewardRuleRequest;
import com.moyu.daijia.model.form.rules.RewardRuleRequestForm;
import com.moyu.daijia.model.vo.rules.RewardRuleResponse;
import com.moyu.daijia.model.vo.rules.RewardRuleResponseVo;
import com.moyu.daijia.rules.helper.DroolsHelper;
import com.moyu.daijia.rules.service.RewardRuleService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RewardRuleServiceImpl implements RewardRuleService {

    private static final String RULES_CUSTOMER_RULES_DRL = "rules/RewardRule.drl";

    @Override
    public RewardRuleResponseVo calculateOrderRewardFee(RewardRuleRequestForm rewardRuleRequestForm) {
        RewardRuleRequest rewardRuleRequest = new RewardRuleRequest();
        rewardRuleRequest.setOrderNum(rewardRuleRequestForm.getOrderNum());

        // 创建规则引擎对象
        KieSession kieSession = DroolsHelper.loadForRule(RULES_CUSTOMER_RULES_DRL);

        RewardRuleResponse rewardRuleResponse = new RewardRuleResponse();
        kieSession.setGlobal("rewardRuleResponse", rewardRuleResponse);

        // 设置对象，触发规则
        kieSession.insert(rewardRuleRequest);
        kieSession.fireAllRules();

        // 终止会话
        kieSession.dispose();

        RewardRuleResponseVo rewardRuleResponseVo = new RewardRuleResponseVo();
        log.info("规则引擎计算接单奖励：{}", JSON.toJSONString(rewardRuleResponse));
        rewardRuleResponseVo.setRewardAmount(rewardRuleResponse.getRewardAmount());

        return rewardRuleResponseVo;
    }

}
