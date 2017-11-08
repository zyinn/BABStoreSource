package com.sumscope.bab.store.commons.enums;

import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * Created by Administrator on 2017/2/21.
 * 批量导入时的sheet name
 */
public enum BABSheetName implements WEBEnum {

    BAB_STORE_BKB_IN_SHEET_NAME("BAB_STORE_IN_SHEET_NAME","银票票据库存批量导入"),

    BAB_STORE_CBM_IN_SHEET_NAME("BAB_STORE_IN_SHEET_NAME","商票票据库存批量导入"),

    BAB_STORE_OUT_SHEET_NAME("BAB_STORE_OUT_SHEET_NAME","库存导出");

    private String code;

    private String name;

    BABSheetName(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
