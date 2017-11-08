package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 用于前端查询条件的枚举。对应BillType 和BillMedium的组合。
 */
public enum WEBBillType implements WEBEnum {

	ELE_BKB("ELE_BKB","电银", BABBillType.BKB, BABBillMedium.ELE),

	PAP_BKB("PAP_BKB","纸银", BABBillType.BKB, BABBillMedium.PAP),

	ELE_CMB("ELE_CMB","电商", BABBillType.CMB, BABBillMedium.ELE),

	PAP_CMB("PAP_CMB","纸商", BABBillType.CMB, BABBillMedium.PAP);

	private String code;

	private String name;

	private BABBillType babBillType;

	private BABBillMedium babBillMedium;

	WEBBillType(String code, String name, BABBillType billType, BABBillMedium billMedium) {
		this.code = code;
		this.name = name;
		this.babBillType = billType;
		this.babBillMedium = billMedium;
	}




	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	public BABBillType getBabBillType() {
		return babBillType;
	}

	public BABBillMedium getBabBillMedium() {
		return babBillMedium;
	}
}
