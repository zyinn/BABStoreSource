package com.sumscope.bab.store.dao;

import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.util.ValidationUtil;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.BillInfoValidationModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/28.
 * 接口实现类
 */
@Repository
public class BillInfoDaoImpl implements BillInfoDao {
    private static final String BILL_INFO_INSERT = "com.sumscope.bab.store.mapping.BillInfoMapper.insert";
    private static final String BILL_INFO_UPDATE = "com.sumscope.bab.store.mapping.BillInfoMapper.update";
    private static final String QUERY_BILL_INFO_BY_KEYS= "com.sumscope.bab.store.mapping.BillInfoMapper.getBillInfoByKeys";
    private static final String QUERY_BILL_INFO_BY_ID = "com.sumscope.bab.store.mapping.BillInfoMapper.getBillInfoById";
    private static final String QUERY_BILL_INFO_BY_BILLNUMBER= "com.sumscope.bab.store.mapping.BillInfoMapper.searchBillInfoByNumber";

    @Autowired
    @Qualifier(value = BabStoreConstant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void insertNewBillInfo(BillInfoModel billInfoModel) {
        validateModelBeforeInsert(billInfoModel);
        sqlSessionTemplate.insert(BILL_INFO_INSERT,billInfoModel);
    }

    @Override
    public void updateBillInfo(BillInfoModel billInfoModel) {
        validateModelBeforeUpdate(billInfoModel);
        sqlSessionTemplate.update(BILL_INFO_UPDATE,billInfoModel);
    }

    @Override
    public BillInfoModel getBillInfoById(String id) {
        return sqlSessionTemplate.selectOne(QUERY_BILL_INFO_BY_ID,id);
    }

    /**
     * 使用验证框架，在新增数据之前进行数据验证。
     * @param billInfoModel 新增数据。
     *
     */
    private void validateModelBeforeInsert(BillInfoModel billInfoModel){
        ValidationUtil.validateModel(billInfoModel);
    }
    /**
     * 使用验证框架，在更新数据之前进行数据验证。
     * @param billInfoModel 更新数据。
     *
     */
    private void validateModelBeforeUpdate(BillInfoModel billInfoModel){
        ValidationUtil.validateModel(billInfoModel);
    }

    @Override
    public List<BillInfoValidationModel> searchBillInfoByNumber(String billNumber) {
        return sqlSessionTemplate.selectList(QUERY_BILL_INFO_BY_BILLNUMBER,billNumber);
    }

    @Override
    public BillInfoModel getBillInfoByKeys(String billNumber, String operatorId) {
        BillInfoModel model = new BillInfoModel();
        model.setOperatorId(operatorId);
        model.setBillNumber(billNumber);
        return sqlSessionTemplate.selectOne(QUERY_BILL_INFO_BY_KEYS, model);
    }
}
