package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 入库类型枚举
 *   
 * 
 */
public enum BABStoreGoDownType implements DatabaseEnum, WEBEnum {

	/**
	 * 买入票据：BYI （Buying In）
	 */
	BYI("BYI","买入票据"),

	/**
	 * 应收票据：RCB （Receivable）
	 */
	RCB("RCB","应收票据"),

	/**
	 * 签发票据：SGN （Sign）
	 */
	SGN("SGN","签发票据"),

	/**
	 * 其他：OTH （Others）
	 */
	OTH("OTH","其他");

	private String dbCode;

	private String name;

	BABStoreGoDownType(String code, String name) {
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
