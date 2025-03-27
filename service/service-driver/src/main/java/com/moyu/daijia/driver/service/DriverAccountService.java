package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.entity.driver.DriverAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.daijia.model.form.driver.TransferForm;

public interface DriverAccountService extends IService<DriverAccount> {


    /**
     * 转账
     *
     * @param transferForm
     * @return
     */
    Boolean transfer(TransferForm transferForm);
}
