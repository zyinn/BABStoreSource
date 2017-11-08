package com.sumscope.bab.store.commons.exception;

import com.sumscope.optimus.commons.exceptions.ExceptionType;

/**
 *excel 异常代码
 */
public enum BABExceptionCode implements ExceptionType {
    EXCEL_ERROR("E5001","Excel解析异常"),
    JOIN_USER_CHILD_ERROR("E6001","已是子账户,无法关联其他账户为自己的子账户"),
    JOIN_USER_PARENT_ERROR("E6002","已是母账户,无法关联其他账户为自己的母账户");

    BABExceptionCode(String code, String info) {
        this.code = code;
        this.errorInfoCN = info;
    }

    private String code;
    private String errorInfoCN;

    @Override
    public String getExceptionCode() {
        return code;
    }

    @Override
    public String getExceptionInfoCN() {
        return errorInfoCN;
    }
}
