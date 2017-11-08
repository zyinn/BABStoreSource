package com.sumscope.bab.store.model.dto;

import java.math.BigDecimal;

/**
 * Created by liu.yang on 2017/4/19.
 * 供外部系统成交单使用
 */
public class BillInfoWithGoDownPriceDto extends BillInfoDto {

    private BigDecimal godownPrice;

    public BigDecimal getGodownPrice() {
        return godownPrice;
    }

    public void setGodownPrice(BigDecimal godownPrice) {
        this.godownPrice = godownPrice;
    }
}
