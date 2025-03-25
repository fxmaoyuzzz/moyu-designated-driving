package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson2.JSON;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.driver.client.DriverInfoFeignClient;
import com.moyu.daijia.driver.service.DriverService;
import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService {


    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String login(String code) {
        // 远程调用 得到司机id
        Result<Long> loginResult = driverInfoFeignClient.login(code);
        log.info("调用service-driver服务login接口结果：{}", JSON.toJSONString(loginResult));

        Integer codeResult = loginResult.getCode();
        if (!codeResult.equals(ResultCodeEnum.SUCCESS.getCode())) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
        Long driverId = loginResult.getData();

        // token字符串
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 放到redis 设置过期时间
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token,
                driverId.toString(),
                RedisConstant.USER_LOGIN_KEY_TIMEOUT,
                TimeUnit.SECONDS);

        return token;
    }

    @Override
    public DriverLoginVo getDriverLoginInfo() {
        Long driverId = AuthContextHolder.getUserId();
        log.info("获取ThreadLocal中的司机用户id值：{}", driverId);

        Result<DriverLoginVo> loginVoResult = driverInfoFeignClient.getDriverInfo(driverId);
        log.info("调用service-driver服务getDriverLoginInfo接口结果：{}", JSON.toJSONString(loginVoResult));

        DriverLoginVo driverLoginVo = loginVoResult.getData();

        return driverLoginVo;
    }

    @Override
    public DriverAuthInfoVo getDriverAuthInfo(Long driverId) {
        Result<DriverAuthInfoVo> authInfoVoResult = driverInfoFeignClient.getDriverAuthInfo(driverId);
        log.info("调用service-driver服务getDriverAuthInfo接口结果：{}", JSON.toJSONString(authInfoVoResult));

        DriverAuthInfoVo driverAuthInfoVo = authInfoVoResult.getData();
        return driverAuthInfoVo;
    }

    @Override
    public Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        Result<Boolean> booleanResult = driverInfoFeignClient.updateDriverAuthInfo(updateDriverAuthInfoForm);
        log.info("调用service-driver服务updateDriverAuthInfo接口结果：{}", JSON.toJSONString(booleanResult));

        Boolean data = booleanResult.getData();
        return data;
    }

    @Override
    public Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm) {
        Result<Boolean> booleanResult = driverInfoFeignClient.creatDriverFaceModel(driverFaceModelForm);
        log.info("调用service-driver服务creatDriverFaceModel接口结果：{}", JSON.toJSONString(booleanResult));

        return booleanResult.getData();
    }
}
