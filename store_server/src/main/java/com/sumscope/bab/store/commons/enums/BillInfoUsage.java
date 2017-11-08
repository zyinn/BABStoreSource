package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 票据信息使用状态
 *   
 * 
 */
public enum BillInfoUsage implements WEBEnum {

	/**
	 * 已入库：ITS （In Store）
	 */
	NO_USG("NO_USG","正常"),

	/**
	 * 已出库：OTS （Out Store）
	 */
	USE_BY_OTHERS("USE_BY_OTHERS","被其他用户使用"),

	/**
	 * 已作废：CAL （Canceled）
	 */
	USE_BY_CURRENT("USE_BY_CURRENT","被当前用户使用");

	private String dbCode;

	private String name;

	BillInfoUsage(String code, String name) {
		this.dbCode = code;
		this.name = name;
	}

	@Override
	public String getCode() {
		return this.dbCode;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

}
