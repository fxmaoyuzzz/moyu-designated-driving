package com.moyu.daijia.system.mapper;

import com.moyu.daijia.model.entity.system.SysUser;
import com.moyu.daijia.model.query.system.SysUserQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> selectPage(Page<SysUser> page, @Param("query") SysUserQuery userQuery);
}

