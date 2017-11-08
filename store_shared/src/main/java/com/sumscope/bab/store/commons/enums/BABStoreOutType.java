package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 出库类型枚举
 *   
 * 
 */
public enum BABStoreOutType implements DatabaseEnum, WEBEnum {

	/**
	 * 票据贴现：DSC （Discount）
	 */
	DSC("DSC","票据贴现"),

	/**
	 * 卖出票据：OFK （Offtake）
	 */
	OFK("OFK","卖出票据"),

	/**
	 * 票据质押：PDG（Pledge）
	 */
	PDG("PDG","票据质押"),

	/**
	 * 应付票据：PYB（Payable)
	 */
	PYB("PYB","应付票据"),

	/**
	 * 其他：OTH （Others）
	 */
	OTH("OTH","其他");

	private String dbCode;

	private String name;

	BABStoreOutType(String code, String name) {
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
