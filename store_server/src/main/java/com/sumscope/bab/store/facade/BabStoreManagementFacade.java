package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.model.dto.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 库存管理facade
 */
interface BabStoreManagementFacade {

	/**
	 * 前端使用该服务获取当前有效的票据信息。返回找到的票据信息并同时检查该票据是否被其他用户使用。
	 */
	void getAndCheckBillInfo(HttpServletRequest request, HttpServletResponse response, String billNumber);

	/**
	 * 新增入库
	 */
	void createGoDownStore(HttpServletRequest request, HttpServletResponse response, List<StoreGoDownDto> goDownDto);

	/**
	 * 更新库存信息，前提是库存ID存在且有效
	 */
	void updateGoDownStore(HttpServletRequest request, HttpServletResponse response, StoreGoDownDto storeGoDownDto);

	/**
	 * 出库一条库存信息，前提是ID存在且有效
	 */
	void outStore(HttpServletRequest request, HttpServletResponse response,StoreOutDto storeOutDto);

	/**
	 * 撤销库存信息
	 */
	void cancelStore(HttpServletRequest request, HttpServletResponse response, List<String> storeID);

	/**
	 *发布贴现
     */
	void postDiscount(HttpServletRequest request, HttpServletResponse response,List<BABPostDiscountDto> babPostDiscountDto);

	/**
	 * 根据Excel文件解析入库单
	 */
	void parserGoDownStores(HttpServletRequest request, HttpServletResponse response, ExcelFileDto excelFileDto);

	/**
	 * 导出Excel文件
     */
	void excelExports(HttpServletRequest request, HttpServletResponse response,StoreSearchParamDto param) throws IOException;
	/**
	 *  检验重复提交
	 *
	 */
	void getDataToken(HttpServletRequest request, HttpServletResponse response);

	/**
	 *mock接口
	 */
	void generateMockTestData(HttpServletRequest request, HttpServletResponse response, Map number);
}
