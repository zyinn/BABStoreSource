package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * Created by Administrator on 2017/3/23.
 * 全部页面导出标题
 */
public enum BABAllStoreStatus implements WEBEnum {

    BABStoreStatus("状态"),
    BABStoreType("票类"),
    BillNumber("票号"),
    BABAmount("票面金额"),
    DrawerName("出票人"),
    PayeeName("收款人"),
    AcceptingCompanyName("承兑人"),
    AcceptingCompanyType("承兑人类别"),
    BillStartDate("出票日"),
    BillEndDate("到期日"),
    AdjustDays("调整天数"),
    GoDownDate("入库日期"),
    RemainingTermIn("转入剩余期限"),
    GoDownPrice("入库价格（%）"),
    OutDate("出库日期"),
    RemainingTermOut("转出剩余期限"),
    OutPrice("出库价格（%）"),
    CounterPartyName("转出方"),
    AmountsPayable("应付金额"),
    AmountDue("应收金额"),
    PointDifference("点差"),
    TotalIncome("总收益");

    private String name;

    BABAllStoreStatus( String name) {
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
