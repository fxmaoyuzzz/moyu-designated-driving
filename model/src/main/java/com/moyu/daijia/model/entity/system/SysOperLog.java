package com.moyu.daijia.model.entity.system;

import com.moyu.daijia.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "SysOperLog")
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "模块标题")
	@TableField("title")
	private String title;

	@Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
	@TableField("business_type")
	private String businessType;

	@Schema(description = "方法名称")
	@TableField("method")
	private String method;

	@Schema(description = "请求方式")
	@TableField("request_method")
	private String requestMethod;

	@Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
	@TableField("operator_type")
	private String operatorType;

	@Schema(description = "操作人员")
	@TableField("oper_name")
	private String operName;

	@Schema(description = "部门名称")
	@TableField("dept_name")
	private String deptName;

	@Schema(description = "请求URL")
	@TableField("oper_url")
	private String operUrl;

	@Schema(description = "主机地址")
	@TableField("oper_ip")
	private String operIp;

	@Schema(description = "请求参数")
	@TableField("oper_param")
	private String operParam;

	@Schema(description = "返回参数")
	@TableField("json_result")
	private String jsonResult;

	@Schema(description = "操作状态（0正常 1异常）")
	@TableField("status")
	private Integer status;

	@Schema(description = "错误消息")
	@TableField("error_msg")
	private String errorMsg;

	@Schema(description = "操作时间")
	@TableField("oper_time")
	private Date operTime;

}