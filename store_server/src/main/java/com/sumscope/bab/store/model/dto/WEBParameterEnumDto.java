package com.sumscope.bab.store.model.dto;

public class WEBParameterEnumDto {

	/**
	 * 枚举code
	 * 前端使用code作为参数进行服务端调用
	 */
	private String code;

	/**
	 * 显示名称
	 */
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
