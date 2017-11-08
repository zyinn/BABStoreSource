package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * Created by Administrator on 2017/3/15.
 */
public enum BABStoreConditionName implements WEBEnum {

    BABInStoreType("入库类型"),
    BABOutStoreType("出库类型"),
    AcceptanceBankCategory("承兑人类别"),
    Province("交易地点"),
    TicketType("票据类型");

    private String name;

    BABStoreConditionName( String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
