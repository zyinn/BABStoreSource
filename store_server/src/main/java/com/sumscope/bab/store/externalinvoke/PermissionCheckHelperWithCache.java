package com.sumscope.bab.store.externalinvoke;

import com.sumscope.iam.emclient.model.EmFunctionDto;
import com.sumscope.iam.emclient.model.EmPermissionsResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fan.bai on 2016/12/29.
 * 当用户权限列表较长时，对某一权限的检查将耗费一定时间。该结果应被缓存。因为输入的({@link EmPermissionsResponseDto})对象
 * 也是经过缓存的对象，因此在缓存存活期该对象是唯一的。functionCode对象也是从枚举中获取也唯一。使用本类进行结果缓存。
 */
@Component
class PermissionCheckHelperWithCache {

    /**
     * 根据权限列表判断用户是否具有某个特定的用户权限
     * @param functionCode 功能权限代码
     * @param permissionsResponseDto 权限列表
     * @return 对应功能权限dto，未找到则null
     */
    @Cacheable
    EmFunctionDto checkSpecifiedPermissions(String functionCode, EmPermissionsResponseDto permissionsResponseDto){
        if(StringUtils.isBlank(functionCode)){
            return null;
        }
        List<EmFunctionDto> data = permissionsResponseDto.getData();
        for(EmFunctionDto dto: data){
            if(functionCode.equals(dto.getFunctionCode())){
                return dto;
            }
        }
        return null;

    }
}
