package com.sumscope.bab.store.model.dto;

import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;
import com.sumscope.bab.store.commons.enums.WEBBillType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 库存信息查询条件Dto
 */
public class StoreSearchParamDto {

	/**
	 * 票据状态：
	 * 
	 */
	@NotNull
	private BABStoreStatus babStoreStatus;

	/**
	 * 票据介质，纸票、电票 and
	 * 票据类型，银票、商票
	 */
	private List<WEBBillType> webBillType;
	/**
	 * 入库类型，当查询当前库存时本字段不可为空
	 */
	private List<BABStoreGoDownType> godownType;

	/**
	 * 出库类型,当查询已出库单据时本字段不可为空
	 */
	private List<BABStoreOutType> outType;
	/**
	 * 到期日--开始时间
	 */
	private Date billDueDateStart;
	/**
	 * 到期日--结束时间
	 */
	private Date billDueDateEnd;

	/**
	 * 分页 此字段为true，表示分页
	 */
	private boolean paging;

	private int pageSize;

	private int pageNumber;

	public BABStoreStatus getBabStoreStatus() {
		return babStoreStatus;
	}

	public void setBabStoreStatus(BABStoreStatus babStoreStatus) {
		this.babStoreStatus = babStoreStatus;
	}

	public void setGodownType(List<BABStoreGoDownType> godownType) {
		this.godownType = godownType;
	}

	public void setOutType(List<BABStoreOutType> outType) {
		this.outType = outType;
	}

	public List<BABStoreGoDownType> getGodownType() {
		return godownType;
	}

	public List<BABStoreOutType> getOutType() {
		return outType;
	}

	public Date getBillDueDateStart() {
		return billDueDateStart;
	}

	public void setBillDueDateStart(Date billDueDateStart) {
		this.billDueDateStart = billDueDateStart;
	}

	public Date getBillDueDateEnd() {
		return billDueDateEnd;
	}

	public void setBillDueDateEnd(Date billDueDateEnd) {
		this.billDueDateEnd = billDueDateEnd;
	}

	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<WEBBillType> getWebBillType() {
		return webBillType;
	}

	public void setWebBillType(List<WEBBillType> webBillType) {
		this.webBillType = webBillType;
	}
}
