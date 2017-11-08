package com.sumscope.bab.store.dao;

import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.util.ValidationUtil;
import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.model.*;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 * 接口实现类
 */
@Repository
public class StoreDaoImpl implements StoreDao {

    private static final String INSERT = "com.sumscope.bab.store.mapping.BabStoreMapper.insert";
    private static final String UPDATE = "com.sumscope.bab.store.mapping.BabStoreMapper.update";
    private static final String OUT_STORES = "com.sumscope.bab.store.mapping.BabStoreMapper.outStores";
    private static final String SELECT_STORES = "com.sumscope.bab.store.mapping.BabStoreMapper.getStoreById";
    private static final String QUERY_STORES_INFO = "com.sumscope.bab.store.mapping.BabStoreMapper.searchStoresByParam";
    private static final String VALIDATE_QUERY_STORES_INFO = "com.sumscope.bab.store.mapping.BabStoreMapper.getStoreByKeys";
    private static final String SEARCH_DEAL_STORES_PARAM = "com.sumscope.bab.store.mapping.BabStoreMapper.searchDealStoresByParam";


    @Autowired
    @Qualifier(value = BabStoreConstant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<BabStoreWithInfoModel> searchStoresByParam(StoreSearchParam param) {
        normalizeParameter(param);
        return sqlSessionTemplate.selectList(QUERY_STORES_INFO,param);
    }

    @Override
    public void insertStores(List<BabStoreModel> models) {
        validateModelBeforeInsert(models);
        sqlSessionTemplate.insert(INSERT,models);
    }

    @Override
    public void updateStore(BabStoreModel model) {
        validateModelBeforeUpdate(model);
        sqlSessionTemplate.update(UPDATE,model);
    }

    @Override
    public BabStoreModel getStoreById(String id) {
        return sqlSessionTemplate.selectOne(SELECT_STORES,id);
    }

    @Override
    public void outStores(StoreOutModel outModel) {
        sqlSessionTemplate.update(OUT_STORES,outModel);
    }

    @Override
    public BabStoreWithInfoModel getStoreByKeys(BabStoreModel storeModel) {
        return sqlSessionTemplate.selectOne(VALIDATE_QUERY_STORES_INFO,storeModel);
    }

    @Override
    public BabStoreWithInfoModel searchDealStoresByParam(BillNumberWithOperatorIdDto searchParam) {
        return sqlSessionTemplate.selectOne(SEARCH_DEAL_STORES_PARAM,searchParam);
    }

    /**
     * 使用验证框架，在新增数据之前进行数据验证。
     * @param models 新增数据。
     *
     */
    private void validateModelBeforeInsert(List<BabStoreModel> models){
        for(BabStoreModel model : models){
            ValidationUtil.validateModel(model);
        }
    }
    /**
     * 使用验证框架，在更新数据之前进行数据验证。
     * @param model 更新数据。
     *
     */
    private void validateModelBeforeUpdate(BabStoreModel model){
        ValidationUtil.validateModel(model);
    }
    private void normalizeParameter(StoreSearchParam parameter) {
        if (StringUtils.isEmpty(parameter.getOperatorId())) {
            parameter.setOperatorId(null);
        }
        if(CollectionsUtil.isEmptyOrNullCollection(parameter.getBillTypeModel())){
            parameter.setBillTypeModel(null);
        }
    }
}
