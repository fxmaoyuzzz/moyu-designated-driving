package com.moyu.daijia.driver.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-driver")
public interface DriverInfoFeignClient {


    /**
     * 司机小程序授权登录
     *
     * @param code
     * @return
     */
    @GetMapping("/driver/info/login/{code}")
    Result<Long> login(@PathVariable("code") String code);

    /**
     * 获取司机用户信息
     *
     * @param driverId
     * @return
     */
    @GetMapping("/driver/info/getDriverInfo/{driverId}")
    Result<DriverLoginVo> getDriverInfo(@PathVariable Long driverId);

    /**
     * 获取司机认证信息
     *
     * @param driverId
     * @return
     */
    @GetMapping("/driver/info/getDriverAuthInfo/{driverId}")
    Result<DriverAuthInfoVo> getDriverAuthInfo(@PathVariable("driverId") Long driverId);

    /**
     * 更新司机认证信息
     *
     * @param updateDriverAuthInfoForm
     * @return
     */
    @PostMapping("/driver/info/updateDriverAuthInfo")
    Result<Boolean> updateDriverAuthInfo(@RequestBody UpdateDriverAuthInfoForm updateDriverAuthInfoForm);

    /**
     * 创建司机人脸模型
     *
     * @param driverFaceModelForm
     * @return
     */
    @PostMapping("/driver/info/creatDriverFaceModel")
    Result<Boolean> creatDriverFaceModel(@RequestBody DriverFaceModelForm driverFaceModelForm);
}