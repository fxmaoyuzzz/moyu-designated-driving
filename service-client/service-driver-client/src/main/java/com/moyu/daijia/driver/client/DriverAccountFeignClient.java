package com.moyu.daijia.driver.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.driver.TransferForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-driver")
public interface DriverAccountFeignClient {


    /**
     * 转账
     *
     * @param transferForm
     * @return
     */
    @PostMapping("/driver/account/transfer")
    Result<Boolean> transfer(@RequestBody TransferForm transferForm);
}