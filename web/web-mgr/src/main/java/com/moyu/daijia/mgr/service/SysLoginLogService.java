package com.moyu.daijia.mgr.service;

import com.moyu.daijia.model.entity.system.SysLoginLog;
import com.moyu.daijia.model.query.system.SysLoginLogQuery;
import com.moyu.daijia.model.vo.base.PageVo;

public interface SysLoginLogService {

    PageVo<SysLoginLog> findPage(Long page, Long limit, SysLoginLogQuery sysLoginLogQuery);

    /**
     * 记录登录信息
     *
     * @param sysLoginLog
     * @return
     */
    void recordLoginLog(SysLoginLog sysLoginLog);

    SysLoginLog getById(Long id);
}
