package com.moyu.daijia.rules.service;

import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponseVo;

public interface ProfitsharingRuleService {

    /**
     * 计算系统分账费用
     *
     * @param profitsharingRuleRequestForm
     * @return
     */
    ProfitsharingRuleResponseVo calculateOrderProfitsharingFee(ProfitsharingRuleRequestForm profitsharingRuleRequestForm);
}
