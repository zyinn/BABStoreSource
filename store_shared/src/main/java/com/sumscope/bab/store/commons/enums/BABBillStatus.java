package com.sumscope.bab.store.commons.enums;


import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 库存状态枚举，表明库存状态
 *   
 * 
 */
public enum BABBillStatus implements DatabaseEnum, WEBEnum {

	/**
	 * 有效：VLD（Valid）
	 */
	VLD("VLD","有效"),

	/**
	 * 过期：EPD (Expired)
	 */
	EPD("EPD","过期"),

	/**
	 * 异常：ANM (Abnormal)
	 */
	ANM("ANM","异常");

	private String dbCode;

	private String name;

	BABBillStatus(String code, String name) {
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
