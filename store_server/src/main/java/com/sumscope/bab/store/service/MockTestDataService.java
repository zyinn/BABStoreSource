package com.sumscope.bab.store.service;

/**
 * Created by Administrator on 2017/3/17.
 * 生产用于开发测试使用的模拟数据的服务。主要使用随机数方式生成库存单据。
 */
public interface MockTestDataService {
    /**
     * 生成虚拟库存单据数据用于功能开发及测试
     * @param numberOfData 希望生成的库存单据数量
     * @return 实际生成的库存单据数量
     */
    long generateMockTestData(long numberOfData);

}
