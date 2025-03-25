package com.moyu.daijia.model.vo.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CosUploadVo {

    @Schema(description = "上传路径")
    private String url;

    @Schema(description = "回显地址")
    private String showUrl;

}