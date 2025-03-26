package com.moyu.daijia.rules.service.impl;

import com.moyu.daijia.model.form.rules.FeeRuleRequest;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.rules.FeeRuleResponse;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.moyu.daijia.rules.service.FeeRuleService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class FeeRuleServiceImpl implements FeeRuleService {

    @Autowired
    private KieContainer kieContainer;

    @Override
    public FeeRuleResponseVo calculateOrderFee(FeeRuleRequestForm calculateOrderFeeForm) {
        FeeRuleRequest feeRuleRequest = new FeeRuleRequest();
        feeRuleRequest.setDistance(calculateOrderFeeForm.getDistance());
        Date startTime = calculateOrderFeeForm.getStartTime();
        feeRuleRequest.setStartTime(new DateTime(startTime).toString("HH:mm:ss"));
        feeRuleRequest.setWaitMinute(calculateOrderFeeForm.getWaitMinute());

        // 规则引擎Drools
        KieSession kieSession = kieContainer.newKieSession();

        FeeRuleResponse feeRuleResponse = new FeeRuleResponse();
        kieSession.setGlobal("feeRuleResponse", feeRuleResponse);

        kieSession.insert(feeRuleRequest);
        kieSession.fireAllRules();
        kieSession.dispose();

        FeeRuleResponseVo feeRuleResponseVo = new FeeRuleResponseVo();
        BeanUtils.copyProperties(feeRuleResponse, feeRuleResponseVo);

        return feeRuleResponseVo;
    }
}
