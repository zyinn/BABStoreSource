package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.model.dto.StoreSearchParamDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 库存查询Facade接口
 */
public interface BabStoreQueryFacade {

	/**
	 * 按搜索条件搜索库存信息
	 * 
 *  
	 */
	void searchStoresByParam(HttpServletRequest request, HttpServletResponse response, StoreSearchParamDto param);

}
