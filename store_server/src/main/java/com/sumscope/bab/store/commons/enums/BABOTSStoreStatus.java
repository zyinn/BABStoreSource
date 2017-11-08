package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * Created by Administrator on 2017/3/23.
 * 库存中导出标题
 */
public enum BABOTSStoreStatus implements WEBEnum {

    OutDate("出库日期"),
    BABStoreType("票类"),
    BillNumber("票号"),
    BABAmount("票面金额"),
    DrawerName("出票人"),
    PayeeName("收款人"),
    AcceptingCompanyName("承兑人"),
    AcceptingCompanyType("承兑人类别"),
    BillStartDate("出票日"),
    BillEndDate("到期日"),
    CounterPartyName("转出方"),
    OutPrice("出库价格（%）"),
    BABStoreOutType("库存类型");

    private String name;

    BABOTSStoreStatus(String name) {
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
