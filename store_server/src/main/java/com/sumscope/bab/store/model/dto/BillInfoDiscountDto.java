package com.sumscope.bab.store.model.dto;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.commons.BabStoreConstant;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/1.
 */
public class BillInfoDiscountDto {
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
     * 票据到期日期
     */
    private Date dueDate;
    /**
     * 票面金额
     */
    @DecimalMax(value = BabStoreConstant.AMOUNT_MAX_VALUE)
    @DecimalMin(value = BabStoreConstant.AMOUNT_MIN_VALUE)
    private BigDecimal amount;

    /**
     * 承兑机构类型
     *
     */
    @NotNull
    private BABAcceptingCompanyType acceptingCompanyType;

    /**
     * 银行票据：承兑银行名称
     * 商业票据：承兑机构名称
     */
    @NotNull
    private String acceptingCompanyName;


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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BABAcceptingCompanyType getAcceptingCompanyType() {
        return acceptingCompanyType;
    }

    public void setAcceptingCompanyType(BABAcceptingCompanyType acceptingCompanyType) {
        this.acceptingCompanyType = acceptingCompanyType;
    }

    public String getAcceptingCompanyName() {
        return acceptingCompanyName;
    }

    public void setAcceptingCompanyName(String acceptingCompanyName) {
        this.acceptingCompanyName = acceptingCompanyName;
    }
}
