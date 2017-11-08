package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 定义Web页面查询参数输入方式的枚举
 */
public enum WEBSearchParameterMode implements WEBEnum {

	/**
	 * 单选
	 */
	SINGLE,

	/**
	 * 多选
	 */
	MULTIPLE,

	/**
	 * 某个选择区域的参数，例如起始-结束时间
	 */
	RANGE,

	/**
	 * 用户输入或查询的参数
	 */
	SEARCHBOX;


	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}
}
