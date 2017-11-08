package com.sumscope.bab.store.service;

import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;

import java.util.List;

/**
 * Created by fan.bai on 2017/4/5.
 * 库存查询服务接口，与更改接口分开，该接口可向service级，也可以向facade级提供服务
 */
public interface BABStoreQueryService {
    /**
     * 查询入库信息
     */
    List<BabStoreWithInfoModel> searchStoresByParam(StoreSearchParam param, String operatorId);

    /**
     * 根据票号和操作人 ID 查询票据信息
     * 供成交单使用
     */
    BabStoreWithInfoModel searchStoresByBillNumber(BillNumberWithOperatorIdDto model);
}
