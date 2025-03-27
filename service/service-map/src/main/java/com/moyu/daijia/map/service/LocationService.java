package com.moyu.daijia.map.service;

import com.moyu.daijia.model.form.map.OrderServiceLocationForm;
import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.form.map.UpdateDriverLocationForm;
import com.moyu.daijia.model.form.map.UpdateOrderLocationForm;
import com.moyu.daijia.model.vo.map.NearByDriverVo;
import com.moyu.daijia.model.vo.map.OrderLocationVo;
import com.moyu.daijia.model.vo.map.OrderServiceLastLocationVo;

import java.math.BigDecimal;
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

    /**
     * 司机赶往代驾起始点：更新订单地址到缓存
     *
     * @param updateOrderLocationForm
     * @return
     */
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    /**
     * 司机赶往代驾起始点：获取订单经纬度位置
     *
     * @param orderId
     * @return
     */
    OrderLocationVo getCacheOrderLocation(Long orderId);

    /**
     * 批量保存代驾服务订单位置
     *
     * @param orderLocationServiceFormList
     * @return
     */
    Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList);

    /**
     * 获取订单服务最后一个位置信息
     *
     * @param orderId
     * @return
     */
    OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId);

    /**
     * 计算订单实际里程
     *
     * @param orderId
     * @return
     */
    BigDecimal calculateOrderRealDistance(Long orderId);

}
