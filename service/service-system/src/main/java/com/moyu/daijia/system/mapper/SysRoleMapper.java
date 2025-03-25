package com.moyu.daijia.system.mapper;

import com.moyu.daijia.model.entity.system.SysRole;
import com.moyu.daijia.model.query.system.SysRoleQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    IPage<SysRole> selectPage(Page<SysRole> page, @Param("query") SysRoleQuery roleQuery);
}
