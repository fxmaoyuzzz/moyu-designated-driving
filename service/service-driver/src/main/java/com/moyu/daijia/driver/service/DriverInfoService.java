package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.entity.driver.DriverInfo;
import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DriverInfoService extends IService<DriverInfo> {

    /**
     * 司机用户登录
     *
     * @param code
     * @return
     */
    Long login(String code);

    /**
     * 获取司机用户信息
     *
     * @param driverId
     * @return
     */
    DriverLoginVo getDriverInfo(Long driverId);

    /**
     * 获取司机用户认证信息
     *
     * @param driverId
     * @return
     */
    DriverAuthInfoVo getDriverAuthInfo(Long driverId);

    /**
     * 更新司机认证信息
     *
     * @param updateDriverAuthInfoForm
     * @return
     */
    Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm);

    /**
     * 创建司机人脸模型
     *
     * @param driverFaceModelForm
     * @return
     */
    Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm);
}
