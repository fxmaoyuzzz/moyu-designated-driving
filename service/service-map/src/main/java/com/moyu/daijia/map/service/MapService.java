package com.moyu.daijia.map.service;

import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.vo.map.DrivingLineVo;

public interface MapService {

    /**
     * 计算驾驶线路
     *
     * @param calculateDrivingLineForm
     * @return
     */
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);

}
