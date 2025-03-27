package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.form.order.OrderFeeForm;
import com.moyu.daijia.model.form.order.StartDriveForm;
import com.moyu.daijia.model.form.order.UpdateOrderCartForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import com.moyu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.moyu.daijia.model.vo.order.NewOrderDataVo;
import com.moyu.daijia.model.vo.order.OrderInfoVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrderService {


    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 查询司机新订单数据
     *
     * @param driverId
     * @return
     */
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);

    /**
     * 司机抢单
     *
     * @param driverId
     * @param orderId
     * @return
     */
    Boolean robNewOrder(Long driverId, Long orderId);

    /**
     * 司机端查找当前订单
     *
     * @param driverId
     * @return
     */
    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    /**
     * 获取订单账单详细信息
     *
     * @param orderId
     * @param driverId
     * @return
     */
    OrderInfoVo getOrderInfo(Long orderId, Long driverId);

    /**
     * 计算最佳驾驶线路
     *
     * @param calculateDrivingLineForm
     * @return
     */
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);

    /**
     * 司机到达代驾起始地点
     *
     * @param orderId
     * @param driverId
     * @return
     */
    Boolean driverArriveStartLocation(Long orderId, Long driverId);

    /**
     * 更新代驾车辆信息
     *
     * @param updateOrderCartForm
     * @return
     */
    Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm);

    /**
     * 开始代驾服务
     *
     * @param startDriveForm
     * @return
     */
    Boolean startDrive(StartDriveForm startDriveForm);

    /**
     * 结束代驾服务更新订单账单
     *
     * @param orderFeeForm
     * @return
     */
    Boolean endDrive(OrderFeeForm orderFeeForm) throws ExecutionException, InterruptedException;

    /**
     * 获取司机订单分页列表
     *
     * @param driverId
     * @param page
     * @param limit
     * @return
     */
    PageVo findDriverOrderPage(Long driverId, Long page, Long limit);

    /**
     * 司机发送账单信息
     *
     * @param orderId
     * @param driverId
     * @return
     */
    Boolean sendOrderBillInfo(Long orderId, Long driverId);
}
