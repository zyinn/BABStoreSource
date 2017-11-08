package com.sumscope.bab.store.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/4/14.
 */
public class BillNumberWithOperatorIdDto {

    /**
     * 票据号
     */
    @NotNull
    @Length(min = 16,max = 30)
    private String billNumber;

    /**
     * 操作人ID，IAM系统ID
     */
    @NotNull
    private String operatorId;

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
}
