package com.sumscope.bab.store.model.dto;

/**
 * Created by fan.bai on 2017/3/1.
 */

import java.util.List;

/**
 * Excel文件解析报价单结果Dto
 *
 */
public class ExcelParserResultDto {

    /**
     * 成功解析的报价单
     */
    private List<StoreGoDownDto> storeGoDownDtos;

    /**
     * 错误信息列表
     *
     */
    private List<String> invalids;

    public List<StoreGoDownDto> getStoreGoDownDtos() {
        return storeGoDownDtos;
    }

    public void setStoreGoDownDtos(List<StoreGoDownDto> storeGoDownDtos) {
        this.storeGoDownDtos = storeGoDownDtos;
    }

    public List<String> getInvalids() {
        return invalids;
    }

    public void setInvalids(List<String> invalids) {
        this.invalids = invalids;
    }
}

