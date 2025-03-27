package com.moyu.daijia.rules.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequest;
import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponse;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import com.moyu.daijia.rules.helper.DroolsHelper;
import com.moyu.daijia.rules.mapper.ProfitsharingRuleMapper;
import com.moyu.daijia.rules.service.ProfitsharingRuleService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfitsharingRuleServiceImpl implements ProfitsharingRuleService {

    private static final String RULES_CUSTOMER_RULES_DRL = "rules/ProfitsharingRule.drl";

    @Autowired
    private ProfitsharingRuleMapper rewardRuleMapper;

    @Override
    public ProfitsharingRuleResponseVo calculateOrderProfitsharingFee(ProfitsharingRuleRequestForm profitsharingRuleRequestForm) {
        ProfitsharingRuleRequest profitsharingRuleRequest = new ProfitsharingRuleRequest();
        profitsharingRuleRequest.setOrderAmount(profitsharingRuleRequestForm.getOrderAmount());
        profitsharingRuleRequest.setOrderNum(profitsharingRuleRequestForm.getOrderNum());

        KieSession kieSession = DroolsHelper.loadForRule(RULES_CUSTOMER_RULES_DRL);

        ProfitsharingRuleResponse profitsharingRuleResponse = new ProfitsharingRuleResponse();
        kieSession.setGlobal("profitsharingRuleResponse", profitsharingRuleResponse);

        kieSession.insert(profitsharingRuleRequest);
        kieSession.fireAllRules();
        kieSession.dispose();

        ProfitsharingRuleResponseVo profitsharingRuleResponseVo = new ProfitsharingRuleResponseVo();
        log.info("规则引擎计算分账数据结果：{}", JSON.toJSONString(profitsharingRuleResponse));
        BeanUtils.copyProperties(profitsharingRuleResponse, profitsharingRuleResponseVo);

        return profitsharingRuleResponseVo;
    }
}
