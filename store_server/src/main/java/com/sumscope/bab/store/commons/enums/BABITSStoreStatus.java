package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * Created by Administrator on 2017/3/23.
 * 库存中导出标题
 */
public enum BABITSStoreStatus implements WEBEnum {

    GoDownDate("入库日期"),
    BABStoreType("票类"),
    BillNumber("票号"),
    BABAmount("票面金额"),
    DrawerName("出票人"),
    PayeeName("收款人"),
    AcceptingCompanyName("承兑人"),
    AcceptingCompanyType("承兑人类别"),
    BillStartDate("出票日"),
    BillEndDate("到期日"),
    RemainingTerm("剩余期限"),
    BABStoreGoDownType("入库类型"),
    GoDownPrice("入库价格（%）"),
    AmountsPayable("应付金额"),

    TicketDays("持票天数"),
    BestPrice("当日最优价（%）"),
    LowestDiscount("当日最低贴息"),
    BestGathering("当日最优收款"),
    BestIncome("当日最优收益");

    private String name;

    BABITSStoreStatus( String name) {
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
