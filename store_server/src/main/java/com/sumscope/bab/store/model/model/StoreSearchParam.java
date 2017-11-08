package com.sumscope.bab.store.model.model;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;
import java.util.Date;
import java.util.List;

/**
 * 库存信息查询model
 */
public class StoreSearchParam {

	/**
	 * 票据状态：
	 * 
	 */
	private BABStoreStatus babStoreStatus;

	/**
	 * 票据介质，纸票、电票 和
	 * 票据类型，银票、商票 model
	 */
	private List<WEBBillTypeModel> billTypeModel;
	/**
	 * 承兑机构类型
	 * 
	 */
	private BABAcceptingCompanyType acceptingCompanyType;

	/**
	 * 入库类型
	 */
	private List<BABStoreGoDownType> godownType;

	/**
	 * 出库类型
	 */
	private List<BABStoreOutType> outType;

	/**
	 * 票据到期日上限
	 */
	private Date billDueDateStart;
	/**
	 * 票据到期日下限
	 */
	private Date billDueDateEnd;

	/**
	 * 操作人ID，本字段应在service层设置。
	 */
	private String operatorId;
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

	public BABAcceptingCompanyType getAcceptingCompanyType() {
		return acceptingCompanyType;
	}

	public void setAcceptingCompanyType(BABAcceptingCompanyType acceptingCompanyType) {
		this.acceptingCompanyType = acceptingCompanyType;
	}

	public List<BABStoreGoDownType> getGodownType() {
		return godownType;
	}

	public void setGodownType(List<BABStoreGoDownType> godownType) {
		this.godownType = godownType;
	}

	public List<BABStoreOutType> getOutType() {
		return outType;
	}

	public void setOutType(List<BABStoreOutType> outType) {
		this.outType = outType;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

	public List<WEBBillTypeModel> getBillTypeModel() {
		return billTypeModel;
	}

	public void setBillTypeModel(List<WEBBillTypeModel> billTypeModel) {
		this.billTypeModel = billTypeModel;
	}
}
