package com.moyu.daijia.model.vo.system;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "分配菜单")
@Data
public class AssginMenuVo {

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "菜单id列表")
    private List<Long> menuIdList;

}
