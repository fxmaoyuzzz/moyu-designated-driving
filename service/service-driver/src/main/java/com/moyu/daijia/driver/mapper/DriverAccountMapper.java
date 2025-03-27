package com.moyu.daijia.driver.mapper;

import com.moyu.daijia.model.entity.driver.DriverAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface DriverAccountMapper extends BaseMapper<DriverAccount> {


    void add(@Param("driverId") Long driverId, @Param("amount") BigDecimal amount);
}
