package com.moyu.daijia.system.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysDept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-system")
public interface SysDeptFeignClient {

    @GetMapping("/sysDept/getById/{id}")
    Result<SysDept> getById(@PathVariable Long id);

    @PostMapping("/sysDept/save")
    Result<Boolean> save(@RequestBody SysDept sysDept);

    @PutMapping("/sysDept/update")
    Result<Boolean> update(@RequestBody SysDept sysDept);

    @DeleteMapping("/sysDept/remove/{id}")
    Result<Boolean> remove(@PathVariable Long id);

    /**
     * 获取全部部门节点
     *
     * @return
     */
    @GetMapping("/sysDept/findNodes")
    Result<List<SysDept>> findNodes();

    /**
     * 获取用户部门节点
     *
     * @return
     */
    @GetMapping("/sysDept/findUserNodes")
    Result<List<SysDept>> findUserNodes();

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    @GetMapping("/sysDept/updateStatus/{id}/{status}")
    Result<Boolean> updateStatus(@PathVariable Long id, @PathVariable Integer status);

}

