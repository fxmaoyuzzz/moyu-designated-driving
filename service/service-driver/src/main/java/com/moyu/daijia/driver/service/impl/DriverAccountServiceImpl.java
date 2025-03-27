package com.moyu.daijia.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.driver.mapper.DriverAccountDetailMapper;
import com.moyu.daijia.driver.mapper.DriverAccountMapper;
import com.moyu.daijia.driver.service.DriverAccountService;
import com.moyu.daijia.model.entity.driver.DriverAccount;
import com.moyu.daijia.model.entity.driver.DriverAccountDetail;
import com.moyu.daijia.model.form.driver.TransferForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DriverAccountServiceImpl extends ServiceImpl<DriverAccountMapper, DriverAccount> implements DriverAccountService {


    @Autowired
    private DriverAccountMapper driverAccountMapper;

    @Autowired
    private DriverAccountDetailMapper driverAccountDetailMapper;

    @Override
    public Boolean transfer(TransferForm transferForm) {
        LambdaQueryWrapper<DriverAccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccountDetail::getTradeNo, transferForm.getTradeNo());
        Long count = driverAccountDetailMapper.selectCount(wrapper);
        if (count > 0) {
            return true;
        }

        // 添加奖励到司机账户表
        driverAccountMapper.add(transferForm.getDriverId(), transferForm.getAmount());

        // 添加交易记录
        DriverAccountDetail driverAccountDetail = new DriverAccountDetail();
        BeanUtils.copyProperties(transferForm, driverAccountDetail);
        driverAccountDetailMapper.insert(driverAccountDetail);

        return true;
    }
}
