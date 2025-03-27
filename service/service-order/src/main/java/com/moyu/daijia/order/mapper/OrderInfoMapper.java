package com.moyu.daijia.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.daijia.model.vo.order.OrderListVo;
import com.moyu.daijia.model.vo.order.OrderPayVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    IPage<OrderListVo> selectCustomerOrderPage(Page<OrderInfo> pageParam, @Param("customerId") Long customerId);

    IPage<OrderListVo> findDriverOrderPage(Page<OrderInfo> pageParam, @Param("driverId") Long driverId);

    OrderPayVo selectOrderPayVo(@Param("orderNo") String orderNo, @Param("customerId") Long customerId);
}
