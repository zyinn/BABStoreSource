package com.sumscope.bab.store.commons;

/**
 * 常量
 */
public final class BabStoreConstant {
    public static final String DEFAULT_CACHE = "bab.store.default.cache";
    public static final String CACHE_PREFIX = ".BAB_STORE.";

    private BabStoreConstant(){

    }

    //用于定义业务数据库及历史数据库相关数据服务
    public static final String BUSINESS_DATA_SOURCE = "dataSource";
    public static final String BUSINESS_TRANSACTION_MANAGER = "currentTransactionManager";
    public static final String BUSINESS_SQL_SESSION_TEMPLATE = "sqlSessionTemplate";
    public static final String DEFAULT_CACHING_NAME = "defaultcaching";
    public static final String QUOTE_CACHING_NAME = "quotecaching";
    public static final String USER_TOKEN = "Token";

    //报价token的 生效时间 和失效时间(单位为妙) 及 项目标识
    public static final String FROM_PROJECT ="bab_store";
    public static final int EXPIRED_DATE =600;
    public static final int EFFECTIVE_DATE =1;

    public static final String AMOUNT_MAX_VALUE = "9999999999999.99";
    public static final String AMOUNT_MIN_VALUE = "0.01";
    public static final String PRICE_MAX_VALUE = "99.999";
    public static final String PRICE_MIN_VALUE = "0.001";

    public static final int REMAINING_TERM =999999999;
    public static final int TODAY_TIME =24*60*60*1000;
    public static final int YEARS =360;
    //Excel新老版本后缀
    public static final String NEW_EXCEL = "xlsx";
    public static final String OLD_EXCEL = "xls";



}