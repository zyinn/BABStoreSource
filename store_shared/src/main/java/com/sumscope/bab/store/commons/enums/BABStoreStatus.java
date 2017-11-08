package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 票据状态枚举
 *   
 * 
 */
public enum BABStoreStatus implements DatabaseEnum, WEBEnum {

	/**
	 * 已入库：ITS （In Store）
	 */
	ITS("ITS","已入库"),

	/**
	 * 已出库：OTS （Out Store）
	 */
	OTS("OTS","已出库"),

	/**
	 * 已作废：CAL （Canceled）
	 */
	CAL("CAL","已作废");

	private String dbCode;

	private String name;

	BABStoreStatus(String code, String name) {
		this.dbCode = code;
		this.name = name;
	}

	@Override
	public String getDbCode() {
		return this.dbCode;
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
