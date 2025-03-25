package com.moyu.daijia.model.query.system;

import lombok.Data;

@Data
public class SysOperLogQuery {

	private String title;
	private String operName;

	private String createTimeBegin;
	private String createTimeEnd;

}

