package com.sumscope.bab.store.model.dto;

import com.sumscope.bab.store.commons.enums.BillInfoUsage;

/**
 * Created by Administrator on 2017/3/22.
 * 通过验证返回给web端票据信息使用状态和库存信息
 */
public class BillInfoWithUsageDto {
    /**
     * 票据信息使用状态
     */
    private BillInfoUsage billInfoUsage;
    /**
     * 附加票据信息的库存信息Dto
     */
    private BillInfoDto billInfoDto;

    public BillInfoUsage getBillInfoUsage() {
        return billInfoUsage;
    }

    public void setBillInfoUsage(BillInfoUsage billInfoUsage) {
        this.billInfoUsage = billInfoUsage;
    }

    public BillInfoDto getBillInfoDto() {
        return billInfoDto;
    }

    public void setBillInfoDto(BillInfoDto billInfoDto) {
        this.billInfoDto = billInfoDto;
    }
}
