package com.moyu.daijia.system.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysRole;
import com.moyu.daijia.model.query.system.SysRoleQuery;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.system.AssginRoleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "service-system")
public interface SysRoleFeignClient {


    /**
     * 获取全部角色列表
     *
     * @return
     */
    @GetMapping("/sysRole/findAll")
    Result<List<SysRole>> findAll();

    /**
     * 获取分页列表
     *
     * @param page
     * @param limit
     * @param roleQuery
     * @return
     */
    @PostMapping("/sysRole/findPage/{page}/{limit}")
    Result<PageVo<SysRole>> findPage(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @RequestBody SysRoleQuery roleQuery);

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sysRole/getById/{id}")
    Result<SysRole> getById(@PathVariable("id") Long id);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    @PostMapping("/sysRole/save")
    Result<Boolean> save(@RequestBody @Validated SysRole role);

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @PutMapping("/sysRole/update")
    Result<Boolean> update(@RequestBody SysRole role);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/sysRole/remove/{id}")
    Result<Boolean> remove(@PathVariable("id") Long id);

    /**
     * 根据id列表删除
     *
     * @param idList
     * @return
     */
    @DeleteMapping("/sysRole/batchRemove")
    Result<Boolean> batchRemove(@RequestBody List<Long> idList);

    /**
     * 根据用户获取角色数据
     *
     * @param userId
     * @return
     */
    @GetMapping("/sysRole/toAssign/{userId}")
    Result<Map<String, Object>> toAssign(@PathVariable("userId") Long userId);

    /**
     * 根据用户分配角色
     *
     * @param assginRoleVo
     * @return
     */
    @PostMapping("/sysRole/doAssign")
    Result<Boolean> doAssign(@RequestBody AssginRoleVo assginRoleVo);

}

