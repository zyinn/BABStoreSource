package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.commons.enums.BABQuoteUserRights;
import com.sumscope.bab.store.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.store.model.dto.LoginUserDto;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 * 实例类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/storeInit", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationFacadeImpl extends AbstractPerformanceLogFacade implements ApplicationFacade{

    @Autowired
    private ApplicationFacadeService applicationFacadeService;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/initData", method = RequestMethod.POST)
    public void getStoreViewInitData(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> {
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            return applicationFacadeService.getInitData();
        });
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void loginUser(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUserDto loginUserDto) {
        performWithExceptionCatch(response,()->{
            AccessTokenResultDto tokenDto = iamEntitlementCheck.loginUser(loginUserDto.getUserName(),loginUserDto.getPassword());
            iamEntitlementCheck.checkUserFunctionRight(tokenDto.getUserId());
            List<String> userPermitted = iamEntitlementCheck.getUserPermittedFunctions(tokenDto.getUserId());
            return applicationFacadeService.loginUser(tokenDto,userPermitted);
        });
    }
}
