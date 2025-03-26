package com.moyu.daijia.map.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.map.service.MapService;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "地图API接口管理")
@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @Operation(summary = "计算驾驶线路")
    @PostMapping("/calculateDrivingLine")
    public Result<DrivingLineVo> calculateDrivingLine(@RequestBody CalculateDrivingLineForm
                                                              calculateDrivingLineForm) {
        log.info("调用MapController.calculateDrivingLine接口SUCCESS，入参：{}", JSON.toJSONString(calculateDrivingLineForm));
        DrivingLineVo drivingLineVo = mapService.calculateDrivingLine(calculateDrivingLineForm);
        return Result.ok(drivingLineVo);
    }

}

