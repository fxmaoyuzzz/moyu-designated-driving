package com.moyu.daijia.driver.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.common.constant.SystemConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.driver.config.TencentCloudProperties;
import com.moyu.daijia.driver.mapper.*;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.driver.service.DriverInfoService;
import com.moyu.daijia.model.entity.driver.*;
import com.moyu.daijia.model.form.driver.DriverFaceModelForm;
import com.moyu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.moyu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.moyu.daijia.model.vo.driver.DriverInfoVo;
import com.moyu.daijia.model.vo.driver.DriverLoginVo;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class DriverInfoServiceImpl extends ServiceImpl<DriverInfoMapper, DriverInfo> implements DriverInfoService {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private DriverInfoMapper driverInfoMapper;

    @Autowired
    private DriverSetMapper driverSetMapper;

    @Autowired
    private DriverAccountMapper driverAccountMapper;

    @Autowired
    private DriverLoginLogMapper driverLoginLogMapper;

    @Autowired
    private CosService cosService;

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    private DriverFaceRecognitionMapper driverFaceRecognitionMapper;

    @Override
    public Long login(String code) {
        try {
            // 根据code + 小程序id + 秘钥请求微信接口 返回openid
            WxMaJscode2SessionResult sessionInfo =
                    wxMaService.getUserService().getSessionInfo(code);
            String openid = sessionInfo.getOpenid();

            // 根据openid查询是否第一次登录
            LambdaQueryWrapper<DriverInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DriverInfo::getWxOpenId, openid);
            DriverInfo driverInfo = driverInfoMapper.selectOne(wrapper);
            log.info("根据openId查询数据库结果：{}", JSON.toJSONString(driverInfo));

            if (driverInfo == null) {
                // 添加司机基本信息
                driverInfo = new DriverInfo();
                driverInfo.setNickname(String.valueOf(System.currentTimeMillis()));
                driverInfo.setAvatarUrl("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
                driverInfo.setWxOpenId(openid);
                driverInfoMapper.insert(driverInfo);

                // 初始化司机设置
                DriverSet driverSet = new DriverSet();
                driverSet.setDriverId(driverInfo.getId());
                driverSet.setOrderDistance(new BigDecimal(0));
                driverSet.setAcceptDistance(new BigDecimal(SystemConstant.ACCEPT_DISTANCE));
                driverSet.setIsAutoAccept(0);
                driverSetMapper.insert(driverSet);

                // 初始化司机账户信息
                DriverAccount driverAccount = new DriverAccount();
                driverAccount.setDriverId(driverInfo.getId());
                driverAccountMapper.insert(driverAccount);
            }

            // 记录司机登录信息日志
            DriverLoginLog driverLoginLog = new DriverLoginLog();
            driverLoginLog.setDriverId(driverInfo.getId());
            driverLoginLog.setMsg("小程序登录");
            driverLoginLogMapper.insert(driverLoginLog);

            // 返回司机id
            return driverInfo.getId();
        } catch (WxErrorException e) {
            log.warn("调用司机用户登录接口发生异常：{}", e.getMessage());
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public DriverLoginVo getDriverLoginInfo(Long driverId) {
        // 根据司机id获取司机信息
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);
        log.info("根据driverId查询数据库结果：{}", JSON.toJSONString(driverInfo));

        DriverLoginVo driverLoginVo = new DriverLoginVo();
        BeanUtils.copyProperties(driverInfo, driverLoginVo);

        // 是否建档人脸识别
        String faceModelId = driverInfo.getFaceModelId();
        // boolean isArchiveFace = StringUtils.hasText(faceModelId);

        // 为了测试 默认为true
        driverLoginVo.setIsArchiveFace(true);
        return driverLoginVo;
    }

    @Override
    public DriverAuthInfoVo getDriverAuthInfo(Long driverId) {
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);
        DriverAuthInfoVo driverAuthInfoVo = new DriverAuthInfoVo();
        BeanUtils.copyProperties(driverInfo, driverAuthInfoVo);
        driverAuthInfoVo.setIdcardBackShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardBackUrl()));
        driverAuthInfoVo.setIdcardFrontShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardFrontUrl()));
        driverAuthInfoVo.setIdcardHandShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardHandUrl()));
        driverAuthInfoVo.setDriverLicenseFrontShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseFrontUrl()));
        driverAuthInfoVo.setDriverLicenseBackShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseBackUrl()));
        driverAuthInfoVo.setDriverLicenseHandShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseHandUrl()));

        return driverAuthInfoVo;
    }

    @Override
    public Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        Long driverId = updateDriverAuthInfoForm.getDriverId();

        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setId(driverId);
        BeanUtils.copyProperties(updateDriverAuthInfoForm, driverInfo);

        boolean update = this.updateById(driverInfo);

        return update;
    }

    @Override
    public Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm) {
        // 根据司机id获取司机信息
        DriverInfo driverInfo =
                driverInfoMapper.selectById(driverFaceModelForm.getDriverId());
        try {

            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
                    tencentCloudProperties.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, tencentCloudProperties.getRegion(),
                    clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreatePersonRequest req = new CreatePersonRequest();
            // 设置相关值
            req.setGroupId(tencentCloudProperties.getPersionGroupId());
            // 基本信息
            req.setPersonId(String.valueOf(driverInfo.getId()));
            req.setGender(Long.parseLong(driverInfo.getGender()));
            req.setQualityControl(4L);
            req.setUniquePersonControl(4L);
            req.setPersonName(driverInfo.getName());
            req.setImage(driverFaceModelForm.getImageBase64());

            // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
            CreatePersonResponse resp = client.CreatePerson(req);
            // 输出json格式的字符串回包
            System.out.println(AbstractModel.toJsonString(resp));
            String faceId = resp.getFaceId();
            if (StringUtils.hasText(faceId)) {
                driverInfo.setFaceModelId(faceId);
                driverInfoMapper.updateById(driverInfo);
            }
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public DriverSet getDriverSet(Long driverId) {
        LambdaQueryWrapper<DriverSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverSet::getDriverId, driverId);
        DriverSet driverSet = driverSetMapper.selectOne(wrapper);

        return driverSet;
    }

    @Override
    public Boolean isFaceRecognition(Long driverId) {
        // 根据司机id + 当日日期进行查询
        LambdaQueryWrapper<DriverFaceRecognition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverFaceRecognition::getDriverId, driverId);
        wrapper.eq(DriverFaceRecognition::getFaceDate, new DateTime().toString("yyyy-MM-dd"));
        Long count = driverFaceRecognitionMapper.selectCount(wrapper);

        return count != 0;
    }

    @Override
    public Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm) {
        try {
            // 照片比对
            Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred,
                    tencentCloudProperties.getRegion(),
                    clientProfile);
            VerifyFaceRequest req = new VerifyFaceRequest();
            req.setImage(driverFaceModelForm.getImageBase64());
            req.setPersonId(String.valueOf(driverFaceModelForm.getDriverId()));

            VerifyFaceResponse resp = client.VerifyFace(req);
            log.info("调用腾讯云照片验证结果：{}", JSON.toJSONString(resp));

            if (resp.getIsMatch()) {
                // 照片比对成功 静态活体检测
                Boolean isSuccess = this.
                        detectLiveFace(driverFaceModelForm.getImageBase64());
                if (isSuccess) {
                    // 如果静态活体检测通过 添加数据到认证表
                    DriverFaceRecognition driverFaceRecognition = new DriverFaceRecognition();
                    driverFaceRecognition.setDriverId(driverFaceModelForm.getDriverId());
                    driverFaceRecognition.setFaceDate(new Date());
                    driverFaceRecognitionMapper.insert(driverFaceRecognition);
                    return true;
                }
            }
        } catch (TencentCloudSDKException e) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        return false;
    }

    private Boolean detectLiveFace(String imageBase64) {
        try {
            Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            IaiClient client = new IaiClient(cred, tencentCloudProperties.getRegion(),
                    clientProfile);
            DetectLiveFaceRequest req = new DetectLiveFaceRequest();
            req.setImage(imageBase64);
            DetectLiveFaceResponse resp = client.DetectLiveFace(req);
            log.info("调用腾讯云静态活体验证结果：{}", JSON.toJSONString(resp));
            if (resp.getIsLiveness()) {
                return true;
            }
        } catch (TencentCloudSDKException e) {
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
        return false;
    }

    @Override
    public Boolean updateServiceStatus(Long driverId, Integer status) {
        LambdaQueryWrapper<DriverSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverSet::getDriverId, driverId);
        DriverSet driverSet = new DriverSet();
        driverSet.setServiceStatus(status);
        driverSetMapper.update(driverSet, wrapper);
        return true;
    }

    @Override
    public DriverInfoVo getDriverInfo(Long driverId) {
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);

        DriverInfoVo driverInfoVo = new DriverInfoVo();
        BeanUtils.copyProperties(driverInfo, driverInfoVo);

        // 计算驾龄
        int currentYear = new DateTime().getYear();
        // 获取驾驶证初次领证日期
        // driver_license_issue_date
        int firstYear = new DateTime(driverInfo.getDriverLicenseIssueDate()).getYear();
        int driverLicenseAge = currentYear - firstYear;
        driverInfoVo.setDriverLicenseAge(driverLicenseAge);

        return driverInfoVo;
    }

    @Override
    public String getDriverOpenId(Long driverId) {
        LambdaQueryWrapper<DriverInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverInfo::getId, driverId);
        DriverInfo customerInfo = driverInfoMapper.selectOne(wrapper);

        return customerInfo.getWxOpenId();
    }
}