package com.moyu.daijia.map.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.map.config.TencentCloudProperties;
import com.moyu.daijia.map.service.MapService;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        if (calculateDrivingLineForm.getStartPointLatitude().compareTo(BigDecimal.ZERO) == 0
                || calculateDrivingLineForm.getStartPointLongitude().compareTo(BigDecimal.ZERO) == 0
                || calculateDrivingLineForm.getEndPointLatitude().compareTo(BigDecimal.ZERO) == 0
                || calculateDrivingLineForm.getEndPointLongitude().compareTo(BigDecimal.ZERO) == 0) {
            log.warn("调用腾讯云位置服务失败，参数错误");
            throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        String url = "https://apis.map.qq.com/ws/direction/v1/driving/?from={from}&to={to}&key={key}";

        Map<String, String> map = new HashMap();
        // 开始位置
        // 经纬度：比如 北纬40 东经116
        map.put("from", calculateDrivingLineForm.getStartPointLatitude() + "," + calculateDrivingLineForm.getStartPointLongitude());
        // 结束位置
        map.put("to", calculateDrivingLineForm.getEndPointLatitude() + "," + calculateDrivingLineForm.getEndPointLongitude());
        map.put("key", tencentCloudProperties.getKey());

        JSONObject result = restTemplate.getForObject(url, JSONObject.class, map);
        log.info("调用腾讯云位置服务结果：{}", JSON.toJSONString(result));
        // 处理返回结果
        Integer status = result.getIntValue("status");
        if (!status.equals(ResultCodeEnum.MAP_SUCCESS.getCode())) {
            log.warn("调用腾讯云位置服务失败");
            throw new MoyuException(ResultCodeEnum.MAP_FAIL);
        }

        // 获取返回路线信息
        JSONObject route =
                result.getJSONObject("result").getJSONArray("routes").getJSONObject(0);

        DrivingLineVo drivingLineVo = new DrivingLineVo();
        // 预估时间
        drivingLineVo.setDuration(route.getBigDecimal("duration"));
        // 距离  6.583 == 6.58 / 6.59
        drivingLineVo.setDistance(route.getBigDecimal("distance")
                .divide(new BigDecimal(1000))
                .setScale(2, RoundingMode.HALF_UP));
        // 路线
        drivingLineVo.setPolyline(route.getJSONArray("polyline"));

        return drivingLineVo;
    }
}
