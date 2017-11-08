package com.sumscope.bab.store.commons.enums;

/**
 * Created by fan.bai on 2016/12/29.
 * 系统功能权限枚举
 */
public enum BABQuoteUserRights {
    VALID_SUMSCOPE_USER("","用户不合法！"),
    SSR_MANAGEMENT("bab.quote.ssr.management","用户无直贴报价权限！");

    /**
     * 当无权限时的报错信息
     */
    private final String errorMsg;
    /**
     * 功能权限代码
     */
    private final String functionCode;

    BABQuoteUserRights(String functionCode, String errorMsg) {
        this.functionCode = functionCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getFunctionCode() {
        return functionCode;
    }
}
