package com.sumscope.bab.store.model.model;

import com.sumscope.bab.quote.commons.InsertGroup;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import java.math.BigDecimal;
import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import java.util.Date;

import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BABBillStatus;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * 票据信息模型
 */
public class BillInfoModel {

	/**
	 * 票据信息逻辑主键，不可更新
	 */
	@NotNull
	private String id;

	/**
	 * 票据号，不可更新
	 */
	@NotNull
	private String billNumber;

	/**
	 * 操作人ID，IAM系统ID，不可更新
	 */
	@NotNull
	private String operatorId;

	/**
	 * 票据介质，纸票、电票
	 */
	@NotNull
	private BABBillMedium billMedium;

	/**
	 * 票据类型，银票、商票
	 */
	@NotNull
	private BABBillType billType;

	/**
	 * 金额
	 */
	@NotNull
	@DecimalMax(value = BabStoreConstant.AMOUNT_MAX_VALUE)
	@DecimalMin(value = BabStoreConstant.AMOUNT_MIN_VALUE)
	private BigDecimal amount;

	/**
	 * 收款人名称
	 */
	private String payeeName;

	/**
	 * 出票人名称，一般是机构名称
	 */
	private String drawerName;

	/**
	 * 银行票据：承兑银行名称
	 * 商业票据：承兑机构名称
	 */
	@NotNull
	private String acceptingCompanyName;

	/**
	 * 承兑机构类型
	 * 
	 */
	@NotNull
	private BABAcceptingCompanyType acceptingCompanyType;

	/**
	 * 票据出票日期
	 */
	private Date billStartDate;

	/**
	 * 票据到期日期
	 */
	private Date billDueDate;

	/**
	 * 票据图片
	 */
	private String jpgInfo;

	/**
	 * 创建日期，不可更新
	 */
	@NotNull(groups = InsertGroup.class)
	private Date createDate;

	/**
	 * 最后更新日期
	 */
	@NotNull
	private Date latestUpdateDate;

	/**
	 * 票据状态
	 * 
	 */
	@NotNull
	private BABBillStatus billStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}

	public BABBillType getBillType() {
		return billType;
	}

	public void setBillType(BABBillType billType) {
		this.billType = billType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}

	public String getAcceptingCompanyName() {
		return acceptingCompanyName;
	}

	public void setAcceptingCompanyName(String acceptingCompanyName) {
		this.acceptingCompanyName = acceptingCompanyName;
	}

	public BABAcceptingCompanyType getAcceptingCompanyType() {
		return acceptingCompanyType;
	}

	public void setAcceptingCompanyType(BABAcceptingCompanyType acceptingCompanyType) {
		this.acceptingCompanyType = acceptingCompanyType;
	}

	public Date getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(Date billStartDate) {
		this.billStartDate = billStartDate;
	}

	public Date getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(Date billDueDate) {
		this.billDueDate = billDueDate;
	}

	public String getJpgInfo() {
		return jpgInfo;
	}

	public void setJpgInfo(String jpgInfo) {
		this.jpgInfo = jpgInfo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLatestUpdateDate() {
		return latestUpdateDate;
	}

	public void setLatestUpdateDate(Date latestUpdateDate) {
		this.latestUpdateDate = latestUpdateDate;
	}

	public BABBillStatus getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(BABBillStatus billStatus) {
		this.billStatus = billStatus;
	}

}
