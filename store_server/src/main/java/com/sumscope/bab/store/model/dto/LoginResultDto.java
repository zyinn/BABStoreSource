package com.sumscope.bab.store.model.dto;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/23.
 * 库存登录返回web dto
 */
public class LoginResultDto {

    /**
     * 用户token相关信息
     */
    private LoginUserDto currentUser;

    /**
     * 附加的信息。例如一些权限信息等，根据Key值区分
     */
    private Map<String ,Object> info;

    public LoginUserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginUserDto currentUser) {
        this.currentUser = currentUser;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
