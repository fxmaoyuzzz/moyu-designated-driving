package com.moyu.daijia.mgr.service;

import com.moyu.daijia.model.entity.system.SysOperLog;
import com.moyu.daijia.model.query.system.SysOperLogQuery;
import com.moyu.daijia.model.vo.base.PageVo;

public interface SysOperLogService {

    PageVo<SysOperLog> findPage(Long page, Long limit, SysOperLogQuery sysOperLogQuery);

    /**
     * 保存系统日志记录
     */
    void saveSysLog(SysOperLog sysOperLog);

    SysOperLog getById(Long id);
}
