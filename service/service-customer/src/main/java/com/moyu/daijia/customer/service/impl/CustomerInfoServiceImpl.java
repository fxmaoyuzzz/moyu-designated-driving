package com.moyu.daijia.customer.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson2.JSON;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.customer.mapper.CustomerInfoMapper;
import com.moyu.daijia.customer.mapper.CustomerLoginLogMapper;
import com.moyu.daijia.customer.service.CustomerInfoService;
import com.moyu.daijia.model.entity.customer.CustomerInfo;
import com.moyu.daijia.model.entity.customer.CustomerLoginLog;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private CustomerLoginLogMapper customerLoginLogMapper;

    @Override
    public Long login(String code) {
        // 获取code值，使用微信工具包对象，获取微信唯一标识openid
        String openid = null;
        try {
            WxMaJscode2SessionResult sessionInfo =
                    wxMaService.getUserService().getSessionInfo(code);
            openid = sessionInfo.getOpenid();
            log.info("用户登录获取openId：{}", openid);

        } catch (WxErrorException e) {
            log.warn("用户登录接口发生异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }

        // 根据openid查询数据库表，判断是否第一次登录
        // 如果openid不存在返回null，如果存在返回一条记录
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerInfo::getWxOpenId, openid);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(wrapper);

        // 如果第一次登录，添加信息到用户表
        if (customerInfo == null) {
            log.info("用户为第一次登录，保存用户信息到用户信息表");
            customerInfo = new CustomerInfo();
            customerInfo.setNickname(String.valueOf(System.currentTimeMillis()));
            customerInfo.setAvatarUrl("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
            customerInfo.setWxOpenId(openid);
            customerInfoMapper.insert(customerInfo);
        }

        // 记录登录日志信息
        CustomerLoginLog customerLoginLog = new CustomerLoginLog();
        customerLoginLog.setCustomerId(customerInfo.getId());
        customerLoginLog.setMsg("小程序登录");
        customerLoginLogMapper.insert(customerLoginLog);

        return customerInfo.getId();
    }

    @Override
    public CustomerLoginVo getCustomerInfo(Long customerId) {
        // 根据用户id查询用户信息 封装返回
        CustomerInfo customerInfo = customerInfoMapper.selectById(customerId);
        log.info("根据用户id查询用户信息结果：{}", JSON.toJSONString(customerInfo));

        CustomerLoginVo customerLoginVo = new CustomerLoginVo();
        BeanUtils.copyProperties(customerInfo, customerLoginVo);

        String phone = customerInfo.getPhone();
        boolean isBindPhone = StringUtils.hasText(phone);
        customerLoginVo.setIsBindPhone(isBindPhone);

        return customerLoginVo;
    }

    @Override
    public Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm) {
        // 1 根据code值获取微信绑定手机号码
        try {
            WxMaPhoneNumberInfo phoneNoInfo =
                    wxMaService.getUserService().getPhoneNoInfo(updateWxPhoneForm.getCode());
            String phoneNumber = phoneNoInfo.getPhoneNumber();

            // 更新用户信息
            Long customerId = updateWxPhoneForm.getCustomerId();
            CustomerInfo customerInfo = customerInfoMapper.selectById(customerId);
            customerInfo.setPhone(phoneNumber);
            customerInfoMapper.updateById(customerInfo);

            return true;
        } catch (WxErrorException e) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public String getCustomerOpenId(Long customerId) {
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerInfo::getId, customerId);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(wrapper);
        return customerInfo.getWxOpenId();
    }
}
