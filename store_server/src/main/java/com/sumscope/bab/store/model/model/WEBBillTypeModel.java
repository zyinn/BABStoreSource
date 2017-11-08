package com.sumscope.bab.store.model.model;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;

/**
 * Created by Administrator on 2017/4/7.
 * 查询
 */
public class WEBBillTypeModel {
    /**
     * 票据介质，纸票、电票
     */
    private BABBillMedium billMedium;

    /**
     * 票据类型，银票、商票
     */
    private BABBillType billType;

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
}
