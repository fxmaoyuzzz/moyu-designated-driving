package com.moyu.daijia.system.mapper;


import com.moyu.daijia.model.entity.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findListByUserId(@Param("userId") Long userId);
}

