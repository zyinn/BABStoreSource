package com.sumscope.bab.store.model.dto;

/**
 * 票据基本信息以及是否当前被其他用户使用验证信息Dto
 */
public class BillInfoWithValidationDto extends BillInfoDto {

	/**
	 * true:当前票据信息被其他用户使用
	 * false:可以合法使用，无需提示。
	 */
	private boolean usedByOtherStores;

	public boolean isUsedByOtherStores() {
		return usedByOtherStores;
	}

	public void setUsedByOtherStores(boolean usedByOtherStores) {
		this.usedByOtherStores = usedByOtherStores;
	}
}
