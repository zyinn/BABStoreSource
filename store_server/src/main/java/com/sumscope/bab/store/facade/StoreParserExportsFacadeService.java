package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.WEBEnum;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.store.commons.enums.*;
import com.sumscope.bab.store.externalinvoke.BabQuoteHttpClientHelper;
import com.sumscope.bab.store.facade.converter.BabStoreWithInfoDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreSearchParamDtoConverter;
import com.sumscope.bab.store.model.dto.BabStoreWithInfoDto;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.service.BABStoreQueryService;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * Created by Administrator on 2017/3/15.
 * 导出功能  解析Excel文件并生成对应入库单列表
 */
@Component
public class StoreParserExportsFacadeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;
    @Autowired
    private BabStoreWithInfoDtoConverter babStoreWithInfoDtoConverter;
    @Autowired
    private BABStoreQueryService babStoreQueryService;
    @Autowired
    private BabQuoteHttpClientHelper babQuoteHttpClientHelper;

    /**
     * 页面导出
     */
    void getExcelExports(HttpServletResponse response,StoreSearchParamDto param,String userId){
            try {
                List<BabStoreWithInfoDto> data = getBabStoreWithInfoDtos(param,userId);
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition","attachment");
                response.setHeader("filename","excel.xls");
                ServletOutputStream outputStream = response.getOutputStream();
                HSSFWorkbook hssfWorkbook = getHssfWorkbook(data,param.getBabStoreStatus());
                hssfWorkbook.write(outputStream );
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                LogStashFormatUtil.logError(logger,"excel导出失败！",e);
            }
    }

    private List<BabStoreWithInfoDto> getBabStoreWithInfoDtos(StoreSearchParamDto param,String userId) {
        StoreSearchParam storeSearchParam = storeSearchParamDtoConverter.convertToModel(param);
        List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreQueryService.searchStoresByParam(storeSearchParam, userId);
        List<QuotePriceTrendsDto> quotePriceTrendsDtos = babQuoteHttpClientHelper.retrieveCurrentSSRPriceTrend();
        return babStoreWithInfoDtoConverter.convertToDtoList(babStoreWithInfoModels,quotePriceTrendsDtos);
    }

    private HSSFWorkbook getHssfWorkbook( List<BabStoreWithInfoDto> data,BABStoreStatus storeStatus) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        if(data!=null && data.size()>0){
            for(int i=0 ;i<data.size();i++){
                if(storeStatus == BABStoreStatus.OTS){
                    workbook = setTitleAndDataByOTS(data, BABOTSStoreStatus.values(),BABStoreStatus.OTS);
                }else if(storeStatus == BABStoreStatus.ITS){
                    workbook = setTitleAndDataByITS(data, BABITSStoreStatus.values(),BABStoreStatus.ITS);
                }else{
                    workbook = setTitleAndDataByALL(data, BABAllStoreStatus.values(),null);
                }
            }
        }else{
            //数据为空时只导出标题
            setTilteIsNull(storeStatus, workbook);
        }
        return workbook;
    }

    private void setTilteIsNull(BABStoreStatus storeStatus, HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.createSheet("库存导出");
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        setExcelStyle(style,workbook);
        sheet.setDefaultColumnWidth(16);
        HSSFRow row = sheet.createRow(0);
        sheet.autoSizeColumn(2);
        row.setHeight((short) ((short)200*2));
        if(storeStatus == BABStoreStatus.OTS){
            setUtilsBabStoreData(BABOTSStoreStatus.values(), style, row);//生成标题栏
        }else if(storeStatus == BABStoreStatus.ITS){
            setUtilsBabStoreData(BABITSStoreStatus.values(), style, row);//生成标题栏
        }else{
           setUtilsBabStoreData( BABAllStoreStatus.values(), style, row);//生成标题栏
        }
    }

    private HSSFWorkbook setTitleAndDataByALL(List<BabStoreWithInfoDto> data, BABAllStoreStatus[] title,BABStoreStatus storeStatus) {
        return getHssfWorkbook(data, title,storeStatus);
    }
    private HSSFWorkbook setTitleAndDataByITS(List<BabStoreWithInfoDto> data, BABITSStoreStatus[] title,BABStoreStatus storeStatus) {
        return getHssfWorkbook(data, title,storeStatus);
    }

    private HSSFWorkbook setTitleAndDataByOTS(List<BabStoreWithInfoDto> data, BABOTSStoreStatus[] title, BABStoreStatus storeStatus) {
        return getHssfWorkbook(data, title,storeStatus);
    }

    private HSSFWorkbook getHssfWorkbook(List<BabStoreWithInfoDto> data, WEBEnum[] title,BABStoreStatus storeStatus) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("库存信息");
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        setExcelStyle(style,workbook);
        sheet.setDefaultColumnWidth(18);
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) ((short)200*2));

        setUtilsBabStoreData(title, style, row);//生成标题栏
        setBabStoreWithInfoDtoDataUtils(data, title, sheet, style,storeStatus);//生成对应的数据
        return workbook;
    }

    private void setBabStoreWithInfoDtoDataUtils(List<BabStoreWithInfoDto> data, WEBEnum[] title, HSSFSheet sheet,
                                                 HSSFCellStyle style,BABStoreStatus storeStatus) {
        for (int x=1;x<=data.size();x++) {
            HSSFRow rowx = sheet.createRow(x); //第0行为标题行,所以从1开始创建
            rowx.setHeight((short) ((short)200*2));
            for (int j = 0; j < title.length; j++) {
                HSSFCell cell = rowx.createCell(j);
                if(storeStatus == null){
                    setCellAllValues(cell, j,x, data);
                }
                if(storeStatus == BABStoreStatus.ITS){
                    setCellITSValues(cell, j,x, data);
                }
                if(storeStatus == BABStoreStatus.OTS){
                    setCellOTSValues(cell, j,x, data);
                }
                cell.setCellStyle(style);
            }
        }
    }

    /**
     *生成页面的标题栏
     */
    private void setUtilsBabStoreData(WEBEnum[] title, HSSFCellStyle style, HSSFRow row) {
        for(int i=0;i<title.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(title[i].getDisplayName());
            cell.setCellValue(text);
        }
    }
    private void setCellOTSValues(Cell cell, int j, int x, List<BabStoreWithInfoDto> data){
        BabStoreWithInfoDto dto = data.get(x - 1);
        switch (j) {
            case 0:
                Date goDownDate = dto.getGodownDate();
                setCellValueUtils(cell,goDownDate);
                break;
            case 1:
                String nameType = setBillType(dto);
                setCellValueUtils(cell,nameType);
                break;
            case 2:
                String billNumber = dto.getBillInfoDto().getBillNumber();
                setCellValueUtils(cell,billNumber);
                break;
            case 3:
                BigDecimal amount = dto.getBillInfoDto().getAmount();
                setCellValueUtils(cell,amount);
                break;
            case 4:
                String drawerName = dto.getBillInfoDto().getDrawerName();
                setCellValueUtils(cell,drawerName);
                break;
            case 5:
                String payeeName = dto.getBillInfoDto().getPayeeName();
                setCellValueUtils(cell,payeeName);
                break;

            case 6:
                String acceptingCompanyName = dto.getBillInfoDto().getAcceptingCompanyName();
                setCellValueUtils(cell,acceptingCompanyName);
                break;
            case 7:
                cell.setCellValue(dto.getBillInfoDto().getAcceptingCompanyType().getDisplayName());
                break;
            case 8:
                Date billStartDate = dto.getBillInfoDto().getBillStartDate();
                setCellValueUtils(cell,billStartDate);
                break;
            case 9:
                Date billDueDate = dto.getBillInfoDto().getBillDueDate();
                setCellValueUtils(billDueDate,cell);
                break;
            case 10:
                String counterPartyName = dto.getCounterPartyName();
                setCellValueUtils(cell,counterPartyName);
                break;
            case 11:
                setCellValueUtils(cell,dto.getOutPrice());
                break;
            case 12:
                BABStoreOutType outType = dto.getOutType();
                setCellValueUtils(cell,outType!=null ? outType.getDisplayName() : null);
                break;
        }
    }
    private void setCellITSValues(Cell cell, int j, int x, List<BabStoreWithInfoDto> data){
        BabStoreWithInfoDto dto = data.get(x - 1);
        switch (j) {
            case 0:
                Date goDownDate = dto.getGodownDate();
                setCellValueUtils(cell,goDownDate);
                break;
            case 1:
                String nameType = setBillType(dto);
                setCellValueUtils(cell,nameType);
                break;
            case 2:
                String billNumber = dto.getBillInfoDto().getBillNumber();
                setCellValueUtils(cell,billNumber);
                break;
            case 3:
                BigDecimal amount = dto.getBillInfoDto().getAmount();
                setCellValueUtils(cell,amount);
                break;
            case 4:
                String drawerName = dto.getBillInfoDto().getDrawerName();
                setCellValueUtils(cell,drawerName);
                break;
            case 5:
                String payeeName = dto.getBillInfoDto().getPayeeName();
                setCellValueUtils(cell,payeeName);
                break;
            case 6:
                String acceptingCompanyName = dto.getBillInfoDto().getAcceptingCompanyName();
                setCellValueUtils(cell,acceptingCompanyName);
                break;
            case 7:
                cell.setCellValue(dto.getBillInfoDto().getAcceptingCompanyType().getDisplayName());
                break;
            case 8:
                Date billStartDate = dto.getBillInfoDto().getBillStartDate();
                setCellValueUtils(cell,billStartDate);
                break;
            case 9:
                Date billDueDate = dto.getBillInfoDto().getBillDueDate();
                setCellValueUtils(billDueDate,cell);
                break;
            case 10:
                Integer remainingTerm = dto.getBillInfoDto().getTheRemainingTerm();
                setCellValueUtils(cell,remainingTerm);
                break;
            case 11:
                BABStoreGoDownType goDownType = dto.getGodownType();
                setCellValueUtils(cell,goDownType.getDisplayName());
                break;

            case 12:
                BigDecimal goDownPrice = data.get(x - 1).getGodownPrice();
                setCellValueUtils(cell,goDownPrice);
                break;

            case 13:
                BigDecimal amountsPayable = dto.getAmountsPayable();
                setCellValueUtils(cell,amountsPayable);
                break;
            case 14:
                Integer ticketDays = dto.getTicketDays();
                setCellValueUtils(cell,ticketDays);
                break;
            case 15:
                BigDecimal bestPrice = dto.getBestPrice();
                setCellValueUtils(cell,bestPrice);
                break;
            case 16:
                BigDecimal lowestDiscount = dto.getLowestDiscount();
                setCellValueUtils(cell,lowestDiscount);
                break;
            case 17:
                BigDecimal bestGathering = dto.getBestGathering();
                setCellValueUtils(cell,bestGathering);
                break;
            case 18:
                BigDecimal bestIncome = dto.getBestIncome();
                setCellValueUtils(cell,bestIncome);
                break;
        }
    }

    private String setBillType(BabStoreWithInfoDto dto) {
        BABBillType billType = dto.getBillInfoDto().getBillType();
        BABBillMedium billMedium = dto.getBillInfoDto().getBillMedium();
        String nameType = null;
        if(billType == BABBillType.BKB ){
            if(billMedium == BABBillMedium.PAP){
                nameType = "纸银";
            }
            if( billMedium == BABBillMedium.ELE){
                nameType = "电银";
            }
        }
        if(billType == BABBillType.CMB){
            if( billMedium == BABBillMedium.PAP){
                nameType = "纸商";
            }
            if( billMedium == BABBillMedium.ELE){
                nameType = "电商";
            }
        }
        return nameType;
    }

    private void setCellAllValues(Cell cell, int j, int x, List<BabStoreWithInfoDto> data){
            switch (j) {
                case 0:
                    cell.setCellValue(data.get(x - 1).getStoreStatus().getDisplayName());
                    break;
                case 1:
                    String nameType = setBillType(data.get(x - 1));
                    setCellValueUtils(cell,nameType);
                    break;
                case 2:
                    String billNumber = data.get(x - 1).getBillInfoDto().getBillNumber();
                    setCellValueUtils(cell,billNumber);
                    break;
                case 3:
                    BigDecimal amount = data.get(x - 1).getBillInfoDto().getAmount();
                    setCellValueUtils(cell,amount);
                    break;
                case 4:
                    String drawerName = data.get(x - 1).getBillInfoDto().getDrawerName();
                    setCellValueUtils(cell,drawerName);
                    break;
                case 5:
                    String payeeName = data.get(x - 1).getBillInfoDto().getPayeeName();
                    setCellValueUtils(cell,payeeName);
                    break;
                case 6:
                    String acceptingCompanyName = data.get(x - 1).getBillInfoDto().getAcceptingCompanyName();
                    setCellValueUtils(cell,acceptingCompanyName);
                    break;
                case 7:
                    cell.setCellValue(data.get(x - 1).getBillInfoDto().getAcceptingCompanyType().getDisplayName());
                    break;
                case 8:
                    Date billStartDate = data.get(x - 1).getBillInfoDto().getBillStartDate();
                    setCellValueUtils(cell,billStartDate);
                    break;
                case 9:
                    Date billDueDate = data.get(x - 1).getBillInfoDto().getBillDueDate();
                    setCellValueUtils(billDueDate,cell);
                    break;
                case 10:
                    Integer adjustDays = data.get(x - 1).getAdjustDays();
                    setCellValueUtils(cell,adjustDays);
                    break;
                case 11:
                    Date goDownDate = data.get(x - 1).getGodownDate();
                    setCellValueUtils(cell,goDownDate);
                    break;
                case 12:
                    Integer remainingTermIn = data.get(x - 1).getRemainingTermIn();
                    setCellValueUtils(cell,remainingTermIn);
                    break;
                case 13:
                    BigDecimal goDownPrice = data.get(x - 1).getGodownPrice();
                    setCellValueUtils(cell,goDownPrice);
                    break;
                case 14:
                    Date outDate = data.get(x - 1).getOutDate();
                    setCellValueUtils(cell,outDate);
                    break;
                case 15:
                    Integer remainingTermOut = data.get(x - 1).getRemainingTermOut();
                    setCellValueUtils(cell,remainingTermOut);
                    break;
                case 16:
                    BigDecimal outPrice = data.get(x - 1).getOutPrice();
                    setCellValueUtils(cell,outPrice);
                    break;
                case 17:
                    String counterPartyName = data.get(x - 1).getCounterPartyName();
                    setCellValueUtils(cell,counterPartyName);
                    break;
                case 18:
                    BigDecimal amountsPayable = data.get(x - 1).getAmountsPayable();
                    setCellValueUtils(cell,amountsPayable);
                    break;
                case 19:
                    BigDecimal amountDue = data.get(x - 1).getAmountDue();
                    setCellValueUtils(cell,amountDue);
                    break;
                case 20:
                    BigDecimal pointDifference = data.get(x - 1).getPointDifference();
                    setCellValueUtils(cell,pointDifference);
                    break;
                case 21:
                    BigDecimal totalIncome = data.get(x - 1).getTotalIncome();
                    setCellValueUtils(cell,totalIncome);
                    break;
            }
    }

    private void setCellValueUtils(Cell cell,BigDecimal price){
        if(price!=null){
            cell.setCellValue(price.toString());
        }else{
            setDefaultisNull(cell);
        }
    }
    private void setCellValueUtils(Cell cell,Date date){
        if(date!=null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String format = dateFormat.format(date);
                cell.setCellValue(format);
            } catch (Exception e) {
                LogStashFormatUtil.logError(logger,"excel导出日期转换失败！",e);
            }
        }else{
            setDefaultisNull(cell);
        }
    }

    private void setDefaultisNull(Cell cell) {
        cell.setCellValue(" - ");
    }

    private void setCellValueUtils(Date date,Cell cell){
        if(date!=null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd EEEE");
                String format = dateFormat.format(date);
                cell.setCellValue(format);
            } catch (Exception e) {
                LogStashFormatUtil.logError(logger,"excel导出日期转换失败！",e);
            }
        }else{
            setDefaultisNull(cell);
        }
    }
    private void setCellValueUtils(Cell cell,Integer days){
        if( days==null){
            setDefaultisNull(cell);
        }else{
            cell.setCellValue(days);
        }
    }
    private void setCellValueUtils(Cell cell, String value){
        if(value!=null){
            cell.setCellValue(value);
        }else{
            setDefaultisNull(cell);
        }
    }

    /**
     *生成excel 样式
     */
    private void setExcelStyle(HSSFCellStyle style,HSSFWorkbook workbook){
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style.setWrapText(true);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
    }
}
