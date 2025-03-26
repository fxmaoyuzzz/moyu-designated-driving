package com.moyu.daijia.map.service;

import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.vo.map.DrivingLineVo;

public interface MapService {

    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);

}
