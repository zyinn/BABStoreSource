package com.sumscope.bab.store.service;

import com.sumscope.bab.store.model.model.BillInfoModel;

/**
 * Created by fan.bai on 2017/4/5.
 * 票据基本信息服务接口，仅面向服务层，facade不可直接调用该方法，需要调用相应的事务控制标注方法
 */
interface BillInfoManageService {
    /**
     * 根据票据号与操作人ID建立或更新票据信息数据。
     * 先按票据号与操作人查询票据信息，若不存在该数据，insert一条记录；反之则更新该记录。
     * 返回为带ID号的票据信息模型
     */
    BillInfoModel setupBillInfo(BillInfoModel billInfoModel, String operatorId);
}
