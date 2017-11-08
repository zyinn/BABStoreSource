package com.sumscope.bab.store.externalinvoke;

import com.sumscope.bab.store.commons.enums.BABQuoteUserRights;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.iam.emclient.EmHttpClientWithCache;
import com.sumscope.iam.emclient.model.EmFunctionDto;
import com.sumscope.iam.emclient.model.EmPermissionsResponseDto;
import com.sumscope.iam.iamclient.EntitlementFailedException;
import com.sumscope.iam.iamclient.IamHttpClientWithCache;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2016/12/29.
 * 用于向IAM系统进行用户权限验证的跨系统调用的服务。
 */
@Component
public class IAMEntitlementCheck {
    private static final String TOKEN = "token";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";
    @Autowired
    private IamHttpClientWithCache iamHttpClientWithCache;

    @Autowired
    private EmHttpClientWithCache emHttpClientWithCache;

    @Autowired
    private PermissionCheckHelperWithCache permissionCheckHelperWithCache;

    private String getTokenFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(TOKEN);
    }

    public String getUserIdFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(USERID);
    }


    private String getUserNameFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(USERNAME);
    }

    private void checkToken(HttpServletRequest servletRequest)  {
        String username = getUserNameFromRequest(servletRequest);
        String token = getTokenFromRequest(servletRequest);
        Boolean valid = checkTokenByUsernameAndToken(username,token);
        thorwAuthorizeExceptionIfInvalid(valid, BABQuoteUserRights.VALID_SUMSCOPE_USER.getErrorMsg());
    }

    private boolean checkTokenByUsernameAndToken(String username, String token){
        Boolean valid;
        try {
            valid = iamHttpClientWithCache.checkTokenWithCache(username, token);
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        return valid;
    }

    /**
     *验证用户权限并获取用户id
     */
    public String getUserIdAndCheckValidUser(HttpServletRequest request){
        checkValidUserWithSSRManagement(request);
        return getUserIdFromRequest(request);
    }

    /**
     * 根据Token检查用户是否是合法用户并且有全国直贴报价报价权限，该功能权限仅对经过审核的用户开放。
     * 调用时对应的请求Head中需要保存username,token和userID
     * 一般情况下前端Web将控制，无此权限的用户将无法触发相应功能，服务端做双保险，避免错误调用。
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public void checkValidUserWithSSRManagement(HttpServletRequest servletRequest) {
        checkToken(servletRequest);
        checkUserFunctionRight(getUserIdFromRequest(servletRequest));
    }

    /**
     *验证用户权限
     */
    public void checkUserFunctionRight(String userId){
        checkUserFunctionRights(userId,BABQuoteUserRights.SSR_MANAGEMENT);
    }

    void checkUserFunctionRights(String userId, BABQuoteUserRights functionRight) {
        EmPermissionsResponseDto userPermissionsWithCache ;
        try {
            userPermissionsWithCache = emHttpClientWithCache.getUserPermissionsWithCache(userId);
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        EmFunctionDto validPermission = permissionCheckHelperWithCache.checkSpecifiedPermissions(functionRight.getFunctionCode(), userPermissionsWithCache);
        boolean permit = false;
        if(validPermission != null && "1".equals(validPermission.getPermValue())){
            permit = true;
        }
        thorwAuthorizeExceptionIfInvalid(permit,functionRight.getErrorMsg());
    }

    private void thorwAuthorizeExceptionIfInvalid(Boolean valid,String msg) {
        if(!valid){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,msg);
        }
    }

    /**
     * 用户根据用户名，密码进行登录。登录成功获取token
     * @param username 用户名
     * @param password MD5加密后的密码
     * @return 登录信息，若不成功直接抛出登录异常
     */
    public AccessTokenResultDto loginUser(String username, String password) {
        try {
            return iamHttpClientWithCache.loginWithUsernameAndPassword(username,password);
        } catch (EntitlementFailedException | ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
    }

    /**
     * 获取某用户的可用BAB权限列表。可能为一个空的列表，但不会返回null
     * @param userId 用户ID
     * @return 该用户合法的BAB功能权限code列表
     */
    public List<String> getUserPermittedFunctions(String userId){
        List<String> results = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            return results;
        }
        try {
            EmPermissionsResponseDto userPermissions = emHttpClientWithCache.getUserPermissionsWithCache(userId);
            if(userPermissions != null){
                for(BABQuoteUserRights right: BABQuoteUserRights.values()){
                    EmFunctionDto emFunctionDto = permissionCheckHelperWithCache.checkSpecifiedPermissions(right.getFunctionCode(), userPermissions);
                    if(emFunctionDto!=null && "1".equals(emFunctionDto.getPermValue())){
                        results.add(right.getFunctionCode());
                    }
                }
            }
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        return results;
    }

}
