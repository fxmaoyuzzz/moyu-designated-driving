package com.moyu.daijia.dispatch.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.dispatch.client.XxlJobClient;
import com.moyu.daijia.dispatch.mapper.OrderJobMapper;
import com.moyu.daijia.dispatch.service.NewOrderService;
import com.moyu.daijia.map.client.LocationFeignClient;
import com.moyu.daijia.model.entity.dispatch.OrderJob;
import com.moyu.daijia.model.enums.OrderStatus;
import com.moyu.daijia.model.form.map.SearchNearByDriverForm;
import com.moyu.daijia.model.vo.dispatch.NewOrderTaskVo;
import com.moyu.daijia.model.vo.map.NearByDriverVo;
import com.moyu.daijia.model.vo.order.NewOrderDataVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NewOrderServiceImpl implements NewOrderService {


    @Autowired
    private OrderJobMapper orderJobMapper;

    @Autowired
    private XxlJobClient xxlJobClient;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Override
    public Long addAndStartTask(NewOrderTaskVo newOrderTaskVo) {
        // 判断当前订单是否启动任务调度
        LambdaQueryWrapper<OrderJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderJob::getOrderId, newOrderTaskVo.getOrderId());
        OrderJob orderJob = orderJobMapper.selectOne(wrapper);

        // 没有启动，进行操作
        if (orderJob == null) {
            // 创建并启动任务调度

            Long jobId = xxlJobClient.addAndStart("newOrderTaskHandler", "",
                    "0 0/1 * * * ?",
                    "新创建订单任务调度：" + newOrderTaskVo.getOrderId());

            // 记录任务调度信息
            orderJob = new OrderJob();
            orderJob.setOrderId(newOrderTaskVo.getOrderId());
            orderJob.setJobId(jobId);
            orderJob.setParameter(JSONObject.toJSONString(newOrderTaskVo));
            orderJobMapper.insert(orderJob);
        }
        return orderJob.getJobId();
    }

    @Override
    public void executeTask(long jobId) {
        // 根据JOB-Id查询数据库，当前任务是否已经创建
        // 如果没有创建，不往下执行了
        LambdaQueryWrapper<OrderJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderJob::getJobId, jobId);
        OrderJob orderJob = orderJobMapper.selectOne(wrapper);
        if (orderJob == null) {
            log.warn("未获取到当前JOB详情信息");
            return;
        }

        // 查询订单状态 如果当前订单接单状态 继续执行 如果当前订单不是接单状态 停止任务调度
        String jsonString = orderJob.getParameter();
        NewOrderTaskVo newOrderTaskVo = JSONObject.parseObject(jsonString, NewOrderTaskVo.class);

        Long orderId = newOrderTaskVo.getOrderId();
        Integer status = orderInfoFeignClient.getOrderStatus(orderId).getData();
        if (status.intValue() != OrderStatus.WAITING_ACCEPT.getStatus().intValue()) {
            xxlJobClient.stopJob(jobId);
            log.warn("当前JOB对应订单不是等待接单状态");
            return;
        }

        // 远程调用搜索附近满足条件可以接单司机 获取满足可以接单司机集合
        SearchNearByDriverForm searchNearByDriverForm = new SearchNearByDriverForm();
        searchNearByDriverForm.setLongitude(newOrderTaskVo.getStartPointLongitude());
        searchNearByDriverForm.setLatitude(newOrderTaskVo.getStartPointLatitude());
        searchNearByDriverForm.setMileageDistance(newOrderTaskVo.getExpectDistance());
        List<NearByDriverVo> nearByDriverVoList = locationFeignClient.searchNearByDriver(searchNearByDriverForm).getData();
        log.info("调用远程service-map服务searchNearByDriver结果：{}", JSON.toJSONString(nearByDriverVoList));

        // 遍历司机集合 得到每个司机 为每个司机创建临时队列 存储新订单信息
        nearByDriverVoList.forEach(driver -> {
            // 根据订单id生成key
            String repeatKey = RedisConstant.DRIVER_ORDER_REPEAT_LIST + newOrderTaskVo.getOrderId();
            log.info("当前订单缓存key" + repeatKey);
            // 记录司机id，防止重复推送
            Boolean isMember = redisTemplate.opsForSet().isMember(repeatKey, driver.getDriverId());
            if (!isMember) {
                // 把订单信息推送给满足条件多个司机
                redisTemplate.opsForSet().add(repeatKey, driver.getDriverId());
                // 过期时间：15分钟，超过15分钟没有接单自动取消
                redisTemplate.expire(repeatKey,
                        RedisConstant.DRIVER_ORDER_REPEAT_LIST_EXPIRES_TIME,
                        TimeUnit.MINUTES);

                NewOrderDataVo newOrderDataVo = new NewOrderDataVo();
                newOrderDataVo.setOrderId(newOrderTaskVo.getOrderId());
                newOrderDataVo.setStartLocation(newOrderTaskVo.getStartLocation());
                newOrderDataVo.setEndLocation(newOrderTaskVo.getEndLocation());
                newOrderDataVo.setExpectAmount(newOrderTaskVo.getExpectAmount());
                newOrderDataVo.setExpectDistance(newOrderTaskVo.getExpectDistance());
                newOrderDataVo.setExpectTime(newOrderTaskVo.getExpectTime());
                newOrderDataVo.setFavourFee(newOrderTaskVo.getFavourFee());
                newOrderDataVo.setDistance(driver.getDistance());
                newOrderDataVo.setCreateTime(newOrderTaskVo.getCreateTime());
                // 新订单保存司机的临时队列，Redis里面List集合
                String key = RedisConstant.DRIVER_ORDER_TEMP_LIST + driver.getDriverId();
                redisTemplate.opsForList().leftPush(key, JSONObject.toJSONString(newOrderDataVo));
                // 过期时间：1分钟
                redisTemplate.expire(key, RedisConstant.DRIVER_ORDER_TEMP_LIST_EXPIRES_TIME, TimeUnit.MINUTES);
            }
        });
    }

    @Override
    public List<NewOrderDataVo> findNewOrderQueueData(Long driverId) {
        List<NewOrderDataVo> list = new ArrayList<>();
        String key = RedisConstant.DRIVER_ORDER_TEMP_LIST + driverId;
        Long size = redisTemplate.opsForList().size(key);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String content = (String) redisTemplate.opsForList().leftPop(key);
                NewOrderDataVo newOrderDataVo = JSONObject.parseObject(content, NewOrderDataVo.class);
                list.add(newOrderDataVo);
            }
        }
        return list;
    }

    @Override
    public Boolean clearNewOrderQueueData(Long driverId) {
        String key = RedisConstant.DRIVER_ORDER_TEMP_LIST + driverId;
        redisTemplate.delete(key);
        return true;
    }
}
