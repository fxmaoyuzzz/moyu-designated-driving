package com.moyu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TextAuditingVo {

	@Schema(description = "审核结果")
	private String result;

	@Schema(description = "风险关键词")
	private String keywords;

}