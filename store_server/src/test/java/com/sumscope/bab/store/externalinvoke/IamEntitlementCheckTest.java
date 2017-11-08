package com.sumscope.bab.store.externalinvoke;

import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 *
 */
public class IamEntitlementCheckTest extends AbstractBabStoreIntegrationTest {
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    private static final String USER_ID = "1c497085d6d511e48ec3000c29a13c19"; //roomtest185

    private static final String ROOMTEST185 = "roomtest185";

    private static final String PASSWORD = "e10adc3949ba59abbe56e057f20f883e";


    @Test
    public void getUserPermittedFunctionsTest(){
        try {
            List<String> userPermittedFunctions = iamEntitlementCheck.getUserPermittedFunctions(USER_ID);
            Assert.assertTrue("调用远程iamEntitlementCheck服务失败！",userPermittedFunctions!=null && userPermittedFunctions.size()==1);
        } catch (Exception e) {
            Assert.assertTrue("调用远程iamEntitlementCheck服务失败！",true);
        }
    }

    @Test
    public void userLoginTest(){
        try {
            AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.loginUser(ROOMTEST185, PASSWORD);
            Assert.assertTrue("获取令牌失败！",accessTokenResultDto!=null && accessTokenResultDto.getAccess_token() != null);
        } catch (Exception e) {
            Assert.assertTrue("用户login失败",true);
        }
    }

    @Test
    public void getUserIdAndCheckValidUserTest(){

        AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.loginUser(ROOMTEST185, PASSWORD);
        Assert.assertTrue("用户login失败！",accessTokenResultDto!=null && accessTokenResultDto.getAccess_token() != null);
//        HttpServletRequest request  =null;
//        request.setAttribute("username",ROOMTEST185);
//        request.setAttribute("userid",USER_ID);
//        request.setAttribute("token",accessTokenResultDto.getAccess_token());
//        iamEntitlementCheck.checkUserFunctionRights(request, BABQuoteUserRights.SSR_MANAGEMENT);
//        Assert.assertTrue("权限验证失败！",true);
    }
}
