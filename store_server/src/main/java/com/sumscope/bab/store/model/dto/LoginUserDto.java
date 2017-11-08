package com.sumscope.bab.store.model.dto;

/**
 * Created by Administrator on 2017/3/23.
 * 用户登录
 */
public class LoginUserDto {

    /**
     * token过期日期时间
     */
    private int tokenExpiredTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 所在部门
     */
    private String dept;
    /**
     * userId
     */
    private String id;

    public int getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(int tokenExpiredTime) {
        this.tokenExpiredTime = tokenExpiredTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
