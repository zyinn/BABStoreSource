package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.commons.util.StoreDateUtils;
import com.sumscope.bab.store.commons.util.ValidationUtil;
import com.sumscope.bab.store.model.dto.*;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.optimus.commons.exceptions.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 将入库单转换为库存信息model
 */
@Component
public class StoreGoDownDtoConverter {
	/**
	 *StoreGoDownDto 转 StoreGoDownModel
     */
	private StoreGoDownModel convertToStoreGoDownModel(StoreGoDownDto dto) {
		StoreGoDownModel model = new StoreGoDownModel();
		BeanUtils.copyProperties(dto,model);
		model.setMemo(SecurityStringUtil.validateStr(dto.getMemo()));
		model.setId(SecurityStringUtil.validateStr(dto.getId()));
		model.setGodownType(dto.getGodownType()!=null ? dto.getGodownType() : BABStoreGoDownType.BYI);
		model.setBillInfoModelList(convertBillInfoDtoToBillInfoModelList(dto.getBillInfoDtoList()));
		return model;
	}
	/**
	 * 转换入库单Dto为入库单model与票据信息model
	 */
	public StoreGoDownModel convertToModel(StoreGoDownDto dto) {
		ValidationUtil.validateModel(dto);
		billTypeFormatValidate(dto.getBillInfoDtoList(),0);
		StoreGoDownModel model = convertToStoreGoDownModel(dto);
		return model;
	}



	/**
	 * 验证StoreGoDownDto 到 StoreGoDownModel
	 */
	private List<ValidationExceptionDetails> validateGoDownDto(List<StoreGoDownDto> dtoList){
		List<ValidationExceptionDetails> list = new ArrayList<>();
		if(!CollectionsUtil.isEmptyOrNullCollection(dtoList)){
			for(int i=0;i<dtoList.size();i++){
				List<ValidationExceptionDetails> validation = ValidationUtil.validateModelExceptionList(dtoList.get(i),i);
				List<ValidationExceptionDetails> valid = billTypeFormatValidate(dtoList.get(i).getBillInfoDtoList(),i);
				addValidationExceptionDetails(list,validation);
				addValidationExceptionDetails(list,valid);
			}
		}
		return list;
	}
	private void addValidationExceptionDetails(List<ValidationExceptionDetails> list,List<ValidationExceptionDetails> valid){
		if(!CollectionsUtil.isEmptyOrNullCollection(valid)){
			list.addAll(valid);
		}
	}
	/**
	 * 转换入库单DtoList为入库单modelList与票据信息modelList
	 */
	public List<StoreGoDownModel> convertToModelList(List<StoreGoDownDto> dtoList) {

		List<ValidationExceptionDetails> validationList = validateGoDownDto(dtoList);
		if(!CollectionsUtil.isEmptyOrNullCollection(validationList)){
			throw new ValidationException(validationList);
		}

		List<StoreGoDownModel> list = new ArrayList<>();
		for(StoreGoDownDto dto:dtoList){
			list.add(convertToStoreGoDownModel(dto));
		}
		return list;
	}
	public List<StoreGoDownModel> convertModelToList(StoreGoDownModel storeGoDownModel) {
		return Collections.singletonList(storeGoDownModel);
	}
	/**
	 * BillInfoDto 转 BillInfoModel
     */
	public BillInfoModel convertBillInfoDtoToBillInfoModel(BillInfoDto dto){
		ValidationUtil.validateModel(dto);
		BillInfoModel model = new BillInfoModel();
		model.setCreateDate(dto.getCreateDate()!=null ? dto.getCreateDate() : new Date());
		BeanUtils.copyProperties(dto,model);
		return model;
	}

	public BillInfoWithUsageDto convertBillInfoWithUsageDto(BillInfoUsage billInfoUsage,BillInfoModel billInfo){
		BillInfoWithUsageDto dto = new BillInfoWithUsageDto();
		dto.setBillInfoUsage(billInfoUsage);
		dto.setBillInfoDto(convertBillInfoModelToDto(billInfo));
		return dto;
	}

	/**
	 * List<BillInfoDto> 转 List<BillInfoModel>
	 */
	List<BillInfoModel> convertBillInfoDtoToBillInfoModelList(List<BillInfoDto> billInfoDtoList){
		List<BillInfoModel> list = new ArrayList<>();
		for(BillInfoDto dto:billInfoDtoList){
			BillInfoModel model = convertBillInfoDtoToBillInfoModel(dto);
			list.add(model);
		}
		return list;
	}

	/**
	 *BillInfoModel 转 BillInfoDto
     */
	public BillInfoDto convertBillInfoModelToDto(BillInfoModel billInfo){
		if(billInfo!=null){
			BillInfoDto dto = new BillInfoDto();
			BeanUtils.copyProperties(billInfo,dto);
			dto.setCreateDate(StoreDateUtils.getBigoningDateWithSpecifiedTime(dto.getCreateDate()));
			dto.setBillDueDate(dto.getBillDueDate()!=null ? StoreDateUtils.getBigoningDateWithSpecifiedTime(dto.getBillDueDate()) : null);
			dto.setBillStartDate(dto.getBillStartDate()!=null ? StoreDateUtils.getBigoningDateWithSpecifiedTime(dto.getBillStartDate()) : null);
			dto.setLatestUpdateDate(StoreDateUtils.getBigoningDateWithSpecifiedTime(dto.getLatestUpdateDate()));
			return dto;
		}
		return null;
	}

	/**
	 *  验证四种票据格式
	 *  电银：30位，第1位为1
	 *	纸银：16位，第7位为5
	 *	纸商：16位，第7位为6
	 *	电商：30位，第1位为2
	 */
	List<ValidationExceptionDetails> billTypeFormatValidate(List<BillInfoDto> billInfoDtoList,int index){
		List<ValidationExceptionDetails> det = new ArrayList<>();
		if(!CollectionsUtil.isEmptyOrNullCollection(billInfoDtoList)){
			int i=1;
			for(BillInfoDto infoDto : billInfoDtoList){
				if(infoDto.getBillNumber() == null){
					ValidationExceptionDetails details= new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING,
							"billInfoDtoList["+i+"].billNumber", "库存第"+index+"行,票号不能为空!");
					det.add(details);
				}
				validateBillNumber(det,infoDto,BABBillMedium.ELE,BABBillType.BKB,"^1[0-9]{29}$","电银格式：30位，第1位为1",i,index);
				validateBillNumber(det,infoDto,BABBillMedium.ELE,BABBillType.CMB,"^2[0-9]{29}$","电商格式：30位，第1位为2",i,index);
				validateBillNumber(det,infoDto,BABBillMedium.PAP,BABBillType.BKB,"^[0-9]{6}5[0-9]{9}$","纸银格式：16位，第7位为5",i,index);
				validateBillNumber(det,infoDto,BABBillMedium.PAP,BABBillType.CMB,"^[0-9]{6}6[0-9]{9}$","纸商格式：16位，第7位为6",i,index);
				i++;
			}
		}
		return det;
	}

	private static void validateBillNumber(List<ValidationExceptionDetails> det,BillInfoDto infoDto, WEBEnum billMedium,
										   DatabaseEnum billType,String pattern, String msg ,int i,int index) {
		if(infoDto.getBillMedium()== billMedium && infoDto.getBillType()==billType) {
			if (!Pattern.compile(pattern).matcher(infoDto.getBillNumber()).matches()){
				ValidationExceptionDetails details= new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING,
						"billInfoDtoList["+i+"].billNumber", "库存第"+index+"行,"+msg);
				det.add(details);
			}
			infoDto.setBillNumber(infoDto.getBillNumber());
		}
	}

	public BillNumberWithOperatorIdDto converterBillNumberWithOperatorIdDtoToModel(BillNumberWithOperatorIdDto dto){
		BillNumberWithOperatorIdDto searchParam = new BillNumberWithOperatorIdDto();
		searchParam.setBillNumber(SecurityStringUtil.validateStr(dto.getBillNumber()));
		searchParam.setOperatorId(SecurityStringUtil.validateStr(dto.getOperatorId()));
		return searchParam;
	}

	public BillInfoWithGoDownPriceDto converterBabStoreWithInfoModelToDto(BabStoreWithInfoModel model){
		BillInfoWithGoDownPriceDto dto = new BillInfoWithGoDownPriceDto();
		if(model!=null){
			BeanUtils.copyProperties(model.getBillInfoModel(),dto);
			dto.setGodownPrice(model.getGodownPrice());
		}
		return dto;
	}
}
