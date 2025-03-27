package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.form.map.OrderServiceLocationForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.form.map.UpdateOrderLocationForm;

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
     * 司机赶往代驾起始点：更新订单位置到Redis缓存
     *
     * @param updateOrderLocationForm
     * @return
     */
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    /**
     * 保存代驾服务订单位置
     *
     * @param orderLocationServiceFormList
     * @return
     */
    Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList);
}
