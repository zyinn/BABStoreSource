package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.model.dto.LoginUserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前端应用相关功能facade。例如前端应用第一次打开时通过该接口获取必要的数据并保留在前端，或者定时获取新的token。
 */
public interface ApplicationFacade {

	/**
	 * 获取库存浏览页面初始化数据
	 */
	void getStoreViewInitData(HttpServletRequest request, HttpServletResponse response) ;

	/**
	 *用户登录接口，用户名和密码用heard传递
     */
	void loginUser(HttpServletRequest request, HttpServletResponse response, LoginUserDto loginUserDto);
}
