package com.moyu.daijia.mgr.service.impl;

import com.moyu.daijia.mgr.service.SysDeptService;
import com.moyu.daijia.model.entity.system.SysDept;
import com.moyu.daijia.system.client.SysDeptFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptFeignClient sysDeptFeignClient;


    @Override
    public List<SysDept> findNodes() {
        return sysDeptFeignClient.findNodes().getData();
    }

    @Override
    public List<SysDept> findUserNodes() {
        return sysDeptFeignClient.findUserNodes().getData();
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        sysDeptFeignClient.updateStatus(id, status);
    }

    @Override
    public SysDept getById(Long id) {
        return sysDeptFeignClient.getById(id).getData();
    }

    @Override
    public void save(SysDept sysDept) {
        sysDeptFeignClient.save(sysDept);
    }

    @Override
    public void update(SysDept sysDept) {
        sysDeptFeignClient.update(sysDept);
    }

    @Override
    public void remove(Long id) {
        sysDeptFeignClient.remove(id);
    }
}

