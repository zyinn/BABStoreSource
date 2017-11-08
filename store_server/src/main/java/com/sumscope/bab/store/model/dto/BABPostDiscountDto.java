package com.sumscope.bab.store.model.dto;

import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import javax.validation.Valid;

/**
 * Created by Administrator on 2017/3/31.
 * 发布贴现web参数
 */
public class BABPostDiscountDto {
    /**
     * 发布贴现信息
     */
    @Valid
    private BillInfoDiscountDto billInfoDiscountDto;
    /**
     * 期望价格，可空
     */
    private String price;
    /**
     * 报价区域，新增时如果该类不为null，则code必须存在并检查合法性
     */
    @Valid
    private QuoteProvinceDto quoteProvinces;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public QuoteProvinceDto getQuoteProvinces() {
        return quoteProvinces;
    }

    public void setQuoteProvinces(QuoteProvinceDto quoteProvinces) {
        this.quoteProvinces = quoteProvinces;
    }

    public BillInfoDiscountDto getBillInfoDiscountDto() {
        return billInfoDiscountDto;
    }

    public void setBillInfoDiscountDto(BillInfoDiscountDto billInfoDiscountDto) {
        this.billInfoDiscountDto = billInfoDiscountDto;
    }
}
