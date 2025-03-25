package com.moyu.daijia.customer.service.impl;

import com.alibaba.fastjson2.JSON;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.customer.client.CustomerInfoFeignClient;
import com.moyu.daijia.customer.service.CustomerService;
import com.moyu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.moyu.daijia.model.vo.customer.CustomerLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerInfoFeignClient customerInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String login(String code) {
        //1 使用code进行远程调用 拿到用户id
        Result<Long> loginResult = customerInfoFeignClient.login(code);
        log.info("调用service-customer服务login接口结果：{}", JSON.toJSONString(loginResult));

        Integer codeResult = loginResult.getCode();
        if (!codeResult.equals(ResultCodeEnum.SUCCESS.getCode())) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        Long customerId = loginResult.getData();

        if (customerId == null) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        //生成token字符串
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("当前token：{}", token);

        //把用户id放到Redis 设置过期时间
        // key:token  value:customerId
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token,
                customerId.toString(),
                RedisConstant.USER_LOGIN_KEY_TIMEOUT,
                TimeUnit.SECONDS);

        //7 返回token
        return token;
    }

    @Override
    public CustomerLoginVo getCustomerLoginInfo(String token) {
        //根据token查询redis
        //查询token在redis里面对应用户id
        // String customerId =
        //         (String) redisTemplate.opsForValue()
        //                 .get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);
        // log.info("根据token获取缓存中的用户id值：{}", customerId);
        //
        // if (StringUtils.isEmpty(customerId)) {
        //     throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        // }
        // if (!StringUtils.hasText(customerId)) {
        //     throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        // }

        Long customerId = AuthContextHolder.getUserId();
        log.info("获取ThreadLocal中的用户id值：{}", customerId);


        //根据用户id进行远程调用 得到用户信息
        Result<CustomerLoginVo> customerLoginVoResult =
                customerInfoFeignClient.getCustomerLoginInfo(customerId);
        log.info("调用service-customer服务getCustomerLoginInfo接口结果：{}", JSON.toJSONString(customerLoginVoResult));

        Integer code = customerLoginVoResult.getCode();
        if (!code.equals(ResultCodeEnum.SUCCESS.getCode())) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        CustomerLoginVo customerLoginVo = customerLoginVoResult.getData();
        if (customerLoginVo == null) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        return customerLoginVo;
    }

    @Override
    public Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm) {
        // Result<Boolean> booleanResult = customerInfoFeignClient.updateWxPhoneNumber(updateWxPhoneForm);
        return true;
    }
}
