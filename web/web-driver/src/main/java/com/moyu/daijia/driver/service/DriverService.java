package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;

public interface DriverService {


    /**
     * 司机用户登录
     *
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 获取司机用户登录信息
     *
     * @return
     */
    DriverLoginVo getDriverLoginInfo();

    /**
     * 获取司机认证信息
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

    /**
     * 判断司机当日是否进行过人脸识别
     *
     * @param driverId
     * @return
     */
    Boolean isFaceRecognition(Long driverId);

    /**
     * 验证司机人脸
     *
     * @param driverFaceModelForm
     * @return
     */
    Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm);

    /**
     * 开始接单服务
     *
     * @param driverId
     * @return
     */
    Boolean startService(Long driverId);

    /**
     * 停止接单服务
     *
     * @param driverId
     * @return
     */
    Boolean stopService(Long driverId);
}
