package com.moyu.daijia.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.model.entity.order.*;
import com.moyu.daijia.model.enums.OrderStatus;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.model.form.order.StartDriveForm;
import com.moyu.daijia.model.form.order.UpdateOrderBillForm;
import com.moyu.daijia.model.form.order.UpdateOrderCartForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.order.*;
import com.moyu.daijia.order.mapper.OrderBillMapper;
import com.moyu.daijia.order.mapper.OrderInfoMapper;
import com.moyu.daijia.order.mapper.OrderProfitsharingMapper;
import com.moyu.daijia.order.mapper.OrderStatusLogMapper;
import com.moyu.daijia.order.service.OrderInfoService;
import com.moyu.daijia.order.service.OrderMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderMonitorService orderMonitorService;

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Autowired
    private OrderProfitsharingMapper orderProfitsharingMapper;


    @Override
    public Long saveOrderInfo(OrderInfoForm orderInfoForm) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoForm, orderInfo);

        // TODO: 2024/6/26 后期使用雪花算法生成订单号

        // 订单号
        String orderNo = IdUtil.simpleUUID();
        orderInfo.setOrderNo(orderNo);
        // 订单状态
        orderInfo.setStatus(OrderStatus.WAITING_ACCEPT.getStatus());
        orderInfoMapper.insert(orderInfo);

        // 生成订单之后，发送延迟消息
        // 15分钟超时取消
        this.sendDelayMessage(orderInfo.getId());

        // 记录日志
        this.log(orderInfo.getId(), orderInfo.getStatus());

        redisTemplate.opsForValue().set(RedisConstant.ORDER_ACCEPT_MARK,
                "0", RedisConstant.ORDER_ACCEPT_MARK_EXPIRES_TIME, TimeUnit.MINUTES);

        return orderInfo.getId();
    }

    private void sendDelayMessage(Long orderId) {
        try {
            // 创建队列
            RBlockingQueue<Object> blockingDueue = redissonClient.getBlockingQueue("queue_cancel");

            // 把创建队列放到延迟队列里面
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDueue);

            // 发送消息到延迟队列里面
            // 设置过期时间
            delayedQueue.offer(orderId.toString(), 15, TimeUnit.MINUTES);

        } catch (Exception e) {
            log.warn("发送订单延时消息异常", e);
            e.printStackTrace();
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public Integer getOrderStatus(Long orderId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId, orderId);
        wrapper.select(OrderInfo::getStatus);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        if (orderInfo == null) {
            return OrderStatus.NULL_ORDER.getStatus();
        }

        return orderInfo.getStatus();
    }

    public void log(Long orderId, Integer status) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        orderStatusLog.setOrderStatus(status);
        orderStatusLog.setOperateTime(new Date());
        orderStatusLogMapper.insert(orderStatusLog);
    }

    @Override
    public Boolean robNewOrder(Long driverId, Long orderId) {
        if (!redisTemplate.hasKey(RedisConstant.ORDER_ACCEPT_MARK)) {
            log.warn("当前司机：" + driverId + "抢单失败，订单id：{}", orderId);
            throw new MoyuException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
        }
        // 创建锁
        RLock lock = redissonClient.getLock(RedisConstant.ROB_NEW_ORDER_LOCK + orderId);
        try {
            // 获取锁
            boolean flag = lock.tryLock(RedisConstant.ROB_NEW_ORDER_LOCK_WAIT_TIME, RedisConstant.ROB_NEW_ORDER_LOCK_LEASE_TIME, TimeUnit.SECONDS);
            log.info("司机抢单加锁：{}", flag);
            if (flag) {
                if (!redisTemplate.hasKey(RedisConstant.ORDER_ACCEPT_MARK)) {
                    log.warn("当前司机：" + driverId + "抢单失败，订单id：{}", orderId);
                    // 抢单失败
                    throw new MoyuException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
                }
                LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderInfo::getId, orderId);
                OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
                orderInfo.setStatus(OrderStatus.ACCEPTED.getStatus());
                orderInfo.setDriverId(driverId);
                orderInfo.setAcceptTime(new Date());
                int rows = orderInfoMapper.updateById(orderInfo);
                if (rows != 1) {
                    log.warn("当前司机：" + driverId + "抢单修改数据库订单数据失败，订单id：{}", orderId);
                    throw new MoyuException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
                }

                // 删除抢单标识
                redisTemplate.delete(RedisConstant.ORDER_ACCEPT_MARK);
            }
        } catch (Exception e) {
            // 抢单失败
            log.warn("当前司机：" + driverId + "抢单异常，订单id：{}", orderId, e);
            throw new MoyuException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
        } finally {
            // 释放
            if (lock.isLocked()) {
                log.info("司机抢单删除锁");
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getCustomerId, customerId);

        // 订单状态
        Integer[] statusArray = {
                OrderStatus.ACCEPTED.getStatus(),
                OrderStatus.DRIVER_ARRIVED.getStatus(),
                OrderStatus.UPDATE_CART_INFO.getStatus(),
                OrderStatus.START_SERVICE.getStatus(),
                OrderStatus.END_SERVICE.getStatus(),
                OrderStatus.UNPAID.getStatus()
        };
        wrapper.in(OrderInfo::getStatus, statusArray);

        wrapper.orderByDesc(OrderInfo::getId);
        wrapper.last(" limit 1");

        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        CurrentOrderInfoVo currentOrderInfoVo = new CurrentOrderInfoVo();
        if (orderInfo != null) {
            currentOrderInfoVo.setOrderId(orderInfo.getId());
            currentOrderInfoVo.setStatus(orderInfo.getStatus());
            currentOrderInfoVo.setIsHasCurrentOrder(true);
        } else {
            currentOrderInfoVo.setIsHasCurrentOrder(false);
        }

        return currentOrderInfoVo;
    }

    @Override
    public CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getDriverId, driverId);
        Integer[] statusArray = {
                OrderStatus.ACCEPTED.getStatus(),
                OrderStatus.DRIVER_ARRIVED.getStatus(),
                OrderStatus.UPDATE_CART_INFO.getStatus(),
                OrderStatus.START_SERVICE.getStatus(),
                OrderStatus.END_SERVICE.getStatus(),
                OrderStatus.UNPAID.getStatus(),
                OrderStatus.PAID.getStatus(),
                OrderStatus.FINISH.getStatus()

        };
        wrapper.in(OrderInfo::getStatus, statusArray);
        wrapper.orderByDesc(OrderInfo::getId);
        wrapper.last(" limit 1");
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        CurrentOrderInfoVo currentOrderInfoVo = new CurrentOrderInfoVo();
        if (null != orderInfo) {
            currentOrderInfoVo.setStatus(orderInfo.getStatus());
            currentOrderInfoVo.setOrderId(orderInfo.getId());
            currentOrderInfoVo.setIsHasCurrentOrder(true);
        } else {
            currentOrderInfoVo.setIsHasCurrentOrder(false);
        }

        return currentOrderInfoVo;
    }

    @Override
    public Boolean driverArriveStartLocation(Long orderId, Long driverId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId, orderId);
        wrapper.eq(OrderInfo::getDriverId, driverId);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderStatus.DRIVER_ARRIVED.getStatus());
        orderInfo.setArriveTime(new Date());

        int rows = orderInfoMapper.update(orderInfo, wrapper);

        if (rows == 1) {
            return true;
        } else {
            log.warn("更新订单状态失败，订单id：{}", orderId);
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId, updateOrderCartForm.getOrderId());
        wrapper.eq(OrderInfo::getDriverId, updateOrderCartForm.getDriverId());

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(updateOrderCartForm, orderInfo);
        orderInfo.setStatus(OrderStatus.UPDATE_CART_INFO.getStatus());


        int rows = orderInfoMapper.update(orderInfo, wrapper);
        if (rows == 1) {
            return true;
        } else {
            log.warn("更新车辆信息失败，订单id：{}", updateOrderCartForm.getOrderId());

            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean startDriver(StartDriveForm startDriveForm) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getId, startDriveForm.getOrderId());
        queryWrapper.eq(OrderInfo::getDriverId, startDriveForm.getDriverId());

        OrderInfo updateOrderInfo = new OrderInfo();
        updateOrderInfo.setStatus(OrderStatus.START_SERVICE.getStatus());
        updateOrderInfo.setStartServiceTime(new Date());
        // 只能更新自己的订单
        int row = orderInfoMapper.update(updateOrderInfo, queryWrapper);
        if (row == 1) {
            // 记录日志
            this.log(startDriveForm.getOrderId(), OrderStatus.START_SERVICE.getStatus());
        } else {
            log.warn("更新订单状态失败，订单id：{}", startDriveForm.getOrderId());
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }

        // 初始化订单监控统计数据
        OrderMonitor orderMonitor = new OrderMonitor();
        orderMonitor.setOrderId(startDriveForm.getOrderId());
        orderMonitorService.saveOrderMonitor(orderMonitor);

        return true;
    }

    @Override
    public Long getOrderNumByTime(String startTime, String endTime) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(OrderInfo::getStartServiceTime, startTime);
        wrapper.lt(OrderInfo::getStartServiceTime, endTime);
        Long count = orderInfoMapper.selectCount(wrapper);

        return count;
    }

    @Override
    public Boolean endDrive(UpdateOrderBillForm updateOrderBillForm) {
        // 更新订单信息
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId, updateOrderBillForm.getOrderId());
        wrapper.eq(OrderInfo::getDriverId, updateOrderBillForm.getDriverId());

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderStatus.END_SERVICE.getStatus());
        orderInfo.setRealAmount(updateOrderBillForm.getTotalAmount());
        orderInfo.setFavourFee(updateOrderBillForm.getFavourFee());
        orderInfo.setRealDistance(updateOrderBillForm.getRealDistance());
        orderInfo.setEndServiceTime(new Date());

        int rows = orderInfoMapper.update(orderInfo, wrapper);

        if (rows == 1) {
            log.info("结束代驾更新订单信息成功，添加账单和分账信息");
            // 添加账单数据
            OrderBill orderBill = new OrderBill();
            BeanUtils.copyProperties(updateOrderBillForm, orderBill);
            orderBill.setOrderId(updateOrderBillForm.getOrderId());
            orderBill.setPayAmount(updateOrderBillForm.getTotalAmount());
            orderBillMapper.insert(orderBill);

            // 添加分账信息
            OrderProfitsharing orderProfitsharing = new OrderProfitsharing();
            BeanUtils.copyProperties(updateOrderBillForm, orderProfitsharing);
            orderProfitsharing.setOrderId(updateOrderBillForm.getOrderId());
            orderProfitsharing.setRuleId(updateOrderBillForm.getProfitsharingRuleId());
            orderProfitsharing.setStatus(1);
            orderProfitsharingMapper.insert(orderProfitsharing);

        } else {
            log.warn("结束代驾更新订单信息失败，订单id：{}", updateOrderBillForm.getOrderId());
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }

        return true;
    }

    @Override
    public PageVo findCustomerOrderPage(Page<OrderInfo> pageParam, Long customerId) {
        // TODO: 2024/6/27 优化点：禁止直接查库
        IPage<OrderListVo> pageInfo = orderInfoMapper.selectCustomerOrderPage(pageParam, customerId);
        return new PageVo<>(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Override
    public PageVo findDriverOrderPage(Page<OrderInfo> pageParam, Long driverId) {
        // TODO: 2024/6/27 优化点：禁止直接查库
        IPage<OrderListVo> pageInfo = orderInfoMapper.findDriverOrderPage(pageParam, driverId);
        return new PageVo<>(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Override
    public OrderBillVo getOrderBillInfo(Long orderId) {
        LambdaQueryWrapper<OrderBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderBill::getOrderId, orderId);
        OrderBill orderBill = orderBillMapper.selectOne(wrapper);

        OrderBillVo orderBillVo = new OrderBillVo();
        BeanUtils.copyProperties(orderBill, orderBillVo);
        return orderBillVo;
    }

    @Override
    public OrderProfitsharingVo getOrderProfitsharing(Long orderId) {
        LambdaQueryWrapper<OrderProfitsharing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderProfitsharing::getOrderId, orderId);
        OrderProfitsharing orderProfitsharing = orderProfitsharingMapper.selectOne(wrapper);
        if (orderProfitsharing == null) {
            log.warn("根据订单id获取实际分账信息失败，订单分账信息不存在，订单id：{}", orderId);
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        OrderProfitsharingVo orderProfitsharingVo = new OrderProfitsharingVo();
        BeanUtils.copyProperties(orderProfitsharing, orderProfitsharingVo);
        return orderProfitsharingVo;
    }

    @Override
    public Boolean sendOrderBillInfo(Long orderId, Long driverId) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getId, orderId);
        queryWrapper.eq(OrderInfo::getDriverId, driverId);

        OrderInfo updateOrderInfo = new OrderInfo();
        updateOrderInfo.setStatus(OrderStatus.UNPAID.getStatus());

        int row = orderInfoMapper.update(updateOrderInfo, queryWrapper);
        if (row == 1) {
            return true;
        } else {
            log.warn("司机发送账单信息时更新订单状态失败");
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public OrderPayVo getOrderPayVo(String orderNo, Long customerId) {
        OrderPayVo orderPayVo = orderInfoMapper.selectOrderPayVo(orderNo, customerId);
        if (orderPayVo != null) {
            String content = orderPayVo.getStartLocation() + " 到 " + orderPayVo.getEndLocation();
            orderPayVo.setContent(content);
        }

        return orderPayVo;
    }

    @Override
    public Boolean updateOrderPayStatus(String orderNo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        if (orderInfo == null) {
            log.info("更新订单状态失败，订单不存在，订单号：{}", orderNo);
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }

        if (orderInfo.getStatus().equals(OrderStatus.PAID.getStatus())) {
            log.info("订单已经是已支付状态，订单号：{}", orderNo);
            return true;
        }

        LambdaQueryWrapper<OrderInfo> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(OrderInfo::getOrderNo, orderNo);

        OrderInfo updateOrderInfo = new OrderInfo();
        updateOrderInfo.setStatus(OrderStatus.PAID.getStatus());
        updateOrderInfo.setPayTime(new Date());

        int rows = orderInfoMapper.update(updateOrderInfo, updateWrapper);

        if (rows == 1) {
            return true;
        } else {
            log.warn("更新订单状态失败，订单号：{}", orderNo);
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public OrderRewardVo getOrderRewardFee(String orderNo) {
        OrderInfo orderInfo =
                orderInfoMapper.selectOne(
                        new LambdaQueryWrapper<OrderInfo>()
                                .eq(OrderInfo::getOrderNo, orderNo)
                                .select(OrderInfo::getId, OrderInfo::getDriverId));
        if (orderInfo == null){
            log.warn("获取订单的系统奖励失败，订单不存在，订单号：{}", orderNo);
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }

        OrderBill orderBill =
                orderBillMapper.selectOne(new LambdaQueryWrapper<OrderBill>()
                        .eq(OrderBill::getOrderId, orderInfo.getId())
                        .select(OrderBill::getRewardFee));

        OrderRewardVo orderRewardVo = new OrderRewardVo();
        orderRewardVo.setOrderId(orderInfo.getId());
        orderRewardVo.setDriverId(orderInfo.getDriverId());
        orderRewardVo.setRewardFee(orderBill.getRewardFee());

        return orderRewardVo;
    }

    @Override
    public void orderCancel(long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        if (orderInfo.getStatus().equals(OrderStatus.WAITING_ACCEPT.getStatus())) {
            // 修改订单状态：取消状态
            orderInfo.setStatus(OrderStatus.CANCEL_ORDER.getStatus());
            int rows = orderInfoMapper.updateById(orderInfo);
            if (rows == 1) {
                log.info("订单：{} 超时取消SUCCESS", orderId);
                // 删除接单标识
                redisTemplate.delete(RedisConstant.ORDER_ACCEPT_MARK);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateCouponAmount(Long orderId, BigDecimal couponAmount) {
        int row = orderBillMapper.updateCouponAmount(orderId, couponAmount);
        if (row != 1) {
            log.warn("更新账单优惠券金额失败，订单号：{}", orderId);
            throw new MoyuException(ResultCodeEnum.UPDATE_ERROR);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProfitsharingStatus(String orderNo) {
        // 查询订单
        OrderInfo orderInfo = orderInfoMapper.selectOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderNo, orderNo).select(OrderInfo::getId));

        // 更新状态条件
        LambdaQueryWrapper<OrderProfitsharing> updateQueryWrapper = new LambdaQueryWrapper<>();
        updateQueryWrapper.eq(OrderProfitsharing::getOrderId, orderInfo.getId());
        // 更新字段
        OrderProfitsharing updateOrderProfitsharing = new OrderProfitsharing();
        updateOrderProfitsharing.setStatus(2);
        orderProfitsharingMapper.update(updateOrderProfitsharing, updateQueryWrapper);
    }
}
