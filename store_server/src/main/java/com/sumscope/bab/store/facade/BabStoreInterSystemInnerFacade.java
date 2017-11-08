package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于系统间方法调用的Facade接口
 */
public interface BabStoreInterSystemInnerFacade {

	/**
	 * 按成交单ID查询库存信息列表。
	 */
	void getStoreWithInfoByDealIds(HttpServletRequest request, HttpServletResponse response, String dealId);

	/**
	 * 由外部系统调用，按入库单新增库存信息。返回新增库存信息ID列表。
	 */
	void createGoDownStore(HttpServletRequest request, HttpServletResponse response, StoreGoDownDto goDownDto);
	/**
	 * 根据票号和操作人查询库存信息列表。
	 */
	void getStoreWithInfoByNumberWithOperatorId(HttpServletRequest request, HttpServletResponse response, BillNumberWithOperatorIdDto dto);
}
