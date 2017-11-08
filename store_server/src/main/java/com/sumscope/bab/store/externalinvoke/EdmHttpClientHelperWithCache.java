package com.sumscope.bab.store.externalinvoke;

import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.iam.edmclient.EdmHttpClientWithCache;
import com.sumscope.iam.edmclient.edmsource.dto.IamProvinceAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserRetDTO;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 * edm client
 */
@Component
public class EdmHttpClientHelperWithCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EdmHttpClientWithCache edmHttpClientWithCache;

    public UserRetDTO getUserRetDTO(String userId) {
        try {
            UserRetDTO userByID = edmHttpClientWithCache.getUserByID(userId);
            if (userByID == null) {
                LogStashFormatUtil.logWarrning(logger,"无法根据用户ID从IAM系统获取用户信息！用户ID：" + userId);
            }
            return userByID;
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logWarrning(logger,"根据用户ID从IAM系统获取用户信息失败！" + e);
        }
        return null;
    }

    public UserContactInfoRetDTO getUserContactInfoRetDTO(String userId) {
        try {
            UserContactInfoRetDTO contactByID = edmHttpClientWithCache.getUserContactByID(userId);
            if (contactByID == null) {
                LogStashFormatUtil.logWarrning(logger,"无法根据用户ID从IAM系统获取用户联系信息！用户ID：" + userId);
            }
            return contactByID;
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logWarrning(logger,"根据用户ID从IAM系统获取用户联系信息失败！" + e);
        }
        return null;

    }

    public IdbFinancialCompanyAndIdDTO getIdbFinancialCompanyAndIdDTO(String companyId) {
        try {
            IdbFinancialCompanyAndIdDTO companyInfoByID = edmHttpClientWithCache.getCompanyInfoByID(companyId);
            if (companyInfoByID == null) {
                LogStashFormatUtil.logWarrning(logger,"无法根据ID从IAM系统获取机构信息！ID：" + companyId);
            }
            return companyInfoByID;
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logWarrning(logger,"根据ID从IAM系统获取机构信息失败！" + e);
        }
        return null;
    }


    public List<IamProvinceAndIdDTO> getIamProvinceAndIdDTOToList() {
        try {
            return edmHttpClientWithCache.getIamProvinceAndIdDTOByCountryCode("CN");
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logWarrning(logger,"从IAM系统获取省份信息失败！" + e);
        }
        return null;
    }

}
