package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.util.StoreDateUtils;
import com.sumscope.bab.store.model.dto.BabStoreWithInfoDto;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BabStoreWithInfoDto转换器
 */
@Component
public class BabStoreWithInfoDtoConverter {

	/**
	 * 根据BabStoreWithInfoModel和计算结果（放在list中）构建对应的Dto
	 */
	public List<BabStoreWithInfoDto> convertToDtoList(List<BabStoreWithInfoModel> storeWithInfoModel, List<QuotePriceTrendsDto> quotePriceTrendsDtoList) {
		List<BabStoreWithInfoDto> list = new ArrayList<>();
		if (!CollectionsUtil.isEmptyOrNullCollection(storeWithInfoModel)) {
			for(BabStoreWithInfoModel model : storeWithInfoModel){
				BabStoreWithInfoDto dto = new BabStoreWithInfoDto();
				BeanUtils.copyProperties(model,dto);

				BillInfoDto infoDto = new BillInfoDto();
				BeanUtils.copyProperties(model.getBillInfoModel(),infoDto);
				//剩余期限=到期日-当天日期+调整天
				if(infoDto.getBillDueDate()!=null){
					int theRemainingTerm = setTheRemainingTerm(model, infoDto);
					infoDto.setTheRemainingTerm(theRemainingTerm);//剩余期限
				}

				dto.setBillInfoDto(infoDto);
				//如果为空则不做以下两个方法计算数值。
				if(!CollectionsUtil.isEmptyOrNullCollection(quotePriceTrendsDtoList)){
					setBabStoreByBabQuote(quotePriceTrendsDtoList, dto, infoDto);
				}

				calculateResultDto(model, dto, infoDto);
				list.add(dto);
			}
		}
		return list;
	}

	private int setTheRemainingTerm(BabStoreWithInfoModel model, BillInfoDto infoDto) {
		return (int)(getDateTime(infoDto.getBillDueDate())- getDateTime(new Date()))/(BabStoreConstant.TODAY_TIME) + model.getAdjustDays();
	}

	/**
     *各种需要计算结果的结果值转换
     */
	private void calculateResultDto(BabStoreWithInfoModel model, BabStoreWithInfoDto dto, BillInfoDto infoDto) {
		int remainingTermOut= BabStoreConstant.REMAINING_TERM;//初始化
		int remainingTermGoDown=BabStoreConstant.REMAINING_TERM;//初始化
		Date billDueDate = infoDto.getBillDueDate();
		if(billDueDate!=null){
			Date goDownDate = dto.getGodownDate();
			if(goDownDate!=null){
				int remainingTerm= (int) ((getDateTime(billDueDate) - getDateTime(goDownDate)) / (BabStoreConstant.TODAY_TIME));
				//转入剩余期限
				remainingTermGoDown = remainingTerm + model.getAdjustDays();
			}
			Date outDate = dto.getOutDate();
			if(outDate!=null){
				int remainingTerm = (int) ((getDateTime(billDueDate) - getDateTime(outDate)) / (BabStoreConstant.TODAY_TIME));
				//转出剩余期限
				remainingTermOut = remainingTerm + model.getAdjustDays();
			}
		}

		//应付金额=票面金额－票面金额*入库价格（%）*入库剩余期限÷360
		setAmountsPayableAndBestIncome(dto, infoDto, remainingTermGoDown);

		//应收金额=票面金额－票面金额*出库价格（%）*转出剩余期限÷360
		setAmountDue(dto, infoDto, remainingTermOut);
		//点差=（入库价格%-出库价格%）*100
		setPointDifference(dto);

		//总收益
		if(dto.getAmountDue()!=null && dto.getAmountsPayable()!=null){
			BigDecimal totalIncome = dto.getAmountDue().subtract(dto.getAmountsPayable());
			dto.setTotalIncome(getBigDecimalValue(totalIncome));
		}
		//持票天数=当天日期-入库日期，该值≥0 该值为动态变化
		if(dto.getGodownDate()!=null){
			int days= (int) ((getDateTime(new Date()) - getDateTime(dto.getGodownDate())) / (BabStoreConstant.TODAY_TIME));
			dto.setTicketDays(days);
		}
		if(remainingTermOut!= BabStoreConstant.REMAINING_TERM){
			//转出剩余天数
			dto.setRemainingTermOut(remainingTermOut);
		}
		if(remainingTermGoDown!=BabStoreConstant.REMAINING_TERM){
			//转入剩余天数
			dto.setRemainingTermIn(remainingTermGoDown);
		}
	}

    /**
     *点差
     */
    private void setPointDifference(BabStoreWithInfoDto dto) {
        if(dto.getGodownPrice()!=null && dto.getOutPrice()!=null){
            BigDecimal subtract = dto.getGodownPrice().subtract(dto.getOutPrice());
            BigDecimal  pointDifference= subtract.multiply(new BigDecimal(100));
            dto.setPointDifference(pointDifference);
        }
    }
    /**
     *应付金额  和 当日最优收益
     */
    private void setAmountsPayableAndBestIncome(BabStoreWithInfoDto dto, BillInfoDto infoDto, int remainingTermGoDown) {
        if(remainingTermGoDown != BabStoreConstant.REMAINING_TERM && dto.getGodownPrice()!=null && infoDto!=null && infoDto.getAmount()!=null){
            BigDecimal amountsPayable;
            if(remainingTermGoDown !=0){
                //票面金额*入库价格（%）*入库剩余期限
                BigDecimal basePrice = (infoDto.getAmount().multiply(getDivideBigDecimal(dto.getGodownPrice()))).multiply(new BigDecimal(remainingTermGoDown));
                //票面金额*入库价格（%）*入库剩余期限÷360
                BigDecimal amounts = (basePrice).divide(new BigDecimal(BabStoreConstant.YEARS),4,BigDecimal.ROUND_HALF_UP);
                amountsPayable = infoDto.getAmount().subtract(amounts);
                dto.setAmountsPayable(getBigDecimalValue(amountsPayable));//应付金额
				//当日最优收益=当日最优收款－应付金额
				if(dto.getBestGathering()!=null){
					BigDecimal bestIncome = dto.getBestGathering().subtract(amountsPayable);
					dto.setBestIncome(getBigDecimalValue(bestIncome));
				}
            }
        }
    }

    /**
     *应收金额
     */
    private void setAmountDue(BabStoreWithInfoDto dto, BillInfoDto infoDto, int remainingTermOut) {
        if(remainingTermOut!= BabStoreConstant.REMAINING_TERM && dto.getOutPrice()!=null && infoDto!=null && infoDto.getAmount()!=null){
            BigDecimal amountDue;
            if(remainingTermOut !=0){
                //票面金额*出库价格（%）*转出剩余期限
                BigDecimal multiply = (infoDto.getAmount().multiply(getDivideBigDecimal(dto.getOutPrice()))).multiply(new BigDecimal(remainingTermOut));
                //票面金额*出库价格（%）*转出剩余期限÷360
                BigDecimal baseAmount = (multiply).divide(new BigDecimal(BabStoreConstant.YEARS),4, BigDecimal.ROUND_HALF_UP);
                amountDue = infoDto.getAmount().subtract(baseAmount);//应收金额
                dto.setAmountDue(getBigDecimalValue(amountDue));
            }
        }
    }

	/**
	 *最优收款、当日最优价、最低贴息
     */
	private void setBabStoreByBabQuote(List<QuotePriceTrendsDto> calculationResult, BabStoreWithInfoDto dto, BillInfoDto infoDto) {
		for (QuotePriceTrendsDto trendsDto: calculationResult){
            if(isBooleanByCondition(infoDto, trendsDto) && trendsDto.getPriceMin()!=null){
				//当日最优价 获取价格概览中该票据类型及承兑方类型对应的最低利率
				dto.setBestPrice(trendsDto.getPriceMin());
				//最低贴息=票面金额(元)*(到期日-当天日期+调整天数)*最优价%÷360
				//若未填写到期日期，则无法计算该值，显示为空
				if(infoDto.getBillDueDate()!=null){
					int days= (int) (((getDateTime(infoDto.getBillDueDate()) - getDateTime(new Date())) / BabStoreConstant.TODAY_TIME)+dto.getAdjustDays()) ;
					//(到期日-当天日期+调整天数)*最优价%
					BigDecimal baseMultiply = (trendsDto.getPriceMin().divide(new BigDecimal(100),4, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(days));
					//票面金额(元)*(到期日-当天日期+调整天数)*最优价%÷360
					BigDecimal lowestDiscount = (infoDto.getAmount().multiply(baseMultiply)).divide(new BigDecimal(BabStoreConstant.YEARS),4, BigDecimal.ROUND_HALF_UP);
					dto.setLowestDiscount(getBigDecimalValue(lowestDiscount));//最低贴息
					//当日最优收款=票面金额－最低贴息
					BigDecimal bestGathering = infoDto.getAmount().subtract(lowestDiscount);
					dto.setBestGathering(getBigDecimalValue(bestGathering));
				}
            }
        }
	}

	/**
	 *根据票据类型及承兑方类型
	 *设置对应的最优收款、当日最优价、最低贴息
	 * 最优价获取的是转帖 买断的价格
     */
	private boolean isBooleanByCondition(BillInfoDto infoDto, QuotePriceTrendsDto trendsDto) {
		return trendsDto.getBillMedium() == infoDto.getBillMedium() &&
				trendsDto.getBillType() == infoDto.getBillType() &&
				trendsDto.getQuotePriceType().getCode().equals(infoDto.getAcceptingCompanyType().getCode()) &&
				isBooleanNPCBOT(trendsDto);
	}

	private boolean isBooleanNPCBOT(QuotePriceTrendsDto trendsDto) {
		return BABQuoteType.NPC == trendsDto.getQuoteType() && BABTradeType.BOT == trendsDto.getTradeType() &&
				!trendsDto.getMinorFlag();
	}

	private long getDateTime(Date date){
		return StoreDateUtils.getBeginingTimeByDate(date).getTime();
	}

	private BigDecimal getBigDecimalValue(BigDecimal value){
		if(value == null){
			return null;
		}
		return value.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	private BigDecimal getDivideBigDecimal(BigDecimal value){
		return value.divide(new BigDecimal(100));
	}
}
