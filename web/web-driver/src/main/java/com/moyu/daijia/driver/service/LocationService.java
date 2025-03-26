package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;

public interface LocationService {


    /**
     * 更新司机经纬度位置
     *
     * @param updateDriverLocationForm
     * @return
     */
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

}
