package com.moyu.daijia.map.service;

import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.vo.map.NearByDriverVo;

import java.util.List;

public interface LocationService {

    /**
     * 更新司机经纬度位置
     *
     * @param updateDriverLocationForm
     * @return
     */
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

    /**
     * 删除司机经纬度位置
     *
     * @param driverId
     * @return
     */
    Boolean removeDriverLocation(Long driverId);

    /**
     * 搜索附近满足条件的司机
     *
     * @param searchNearByDriverForm
     * @return
     */
    List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm);
}
