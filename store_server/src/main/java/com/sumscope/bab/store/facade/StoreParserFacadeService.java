package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.WEBBillType;
import com.sumscope.bab.store.commons.exception.BABExceptionCode;
import com.sumscope.bab.store.facade.converter.InvalidsExcelParser;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.ExcelFileDto;
import com.sumscope.bab.store.model.dto.ExcelParserResultDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by fan.bai on 2017/3/1.
 * 解析Excel文件并生成对应入库单列表
 */
@Component
public class StoreParserFacadeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 解析Excel文件并生成对应入库单Dto列表。如果解析过程有错误，将验证异常写入返回Dto。
     * @param excelFileDto Excel文件Dto
     * @param operatorId 操作人ID
     * @return 生成的入库单Dto列表
     */
    public ExcelParserResultDto parserExcelFile(ExcelFileDto excelFileDto, String operatorId){
        if(excelFileDto == null){
            return null;
        }
        ExcelParserResultDto resultDto = new ExcelParserResultDto();
        int start = excelFileDto.getData().indexOf("base64,") + "base64,".length();
        byte[] excelFileContents = Base64.getDecoder().decode(excelFileDto.getData().substring(start));
        String lastName = getExcelLastName(excelFileDto);
        InputStream in = new ByteArrayInputStream(excelFileContents);
        //注意：poi包对excel新/老版本转成workbook对象不同，老版本对应HSSFWorkbook，而新版本对应的是XSSFWorkbook
        //为了兼容新老版本,需要调用官方文档的提供的方法，也即是本类中createWorkbook()方法中实现的那样
        if (lastName.endsWith(BabStoreConstant.NEW_EXCEL)) {
            //获取XSSFWorkbook下的第一页,将字段转换成对应的dto，并验证数据的正确性
            XSSFWorkbook workbook = (XSSFWorkbook) createWorkbook(in);
            converterStoreGoDownDtoAndValidateData(workbook, resultDto, operatorId);
        }
        if(lastName.endsWith(BabStoreConstant.OLD_EXCEL)) {
            //获取HSSFWorkbook下的第一页,将字段转换成对应的dto，并验证数据的正确性
            HSSFWorkbook workbook = (HSSFWorkbook) createWorkbook(in);
            converterStoreGoDownDtoAndValidateData( workbook, resultDto,operatorId);
        }
        return resultDto;
    }

    /**
     *获取List<StoreGoDownDto>方法，并验证模板的正确性
     */
    public List<StoreGoDownDto> converterStoreGoDownDtoAndValidateData(Workbook workbook,ExcelParserResultDto resultDto, String userId) {
        List<StoreGoDownDto> list = new ArrayList<>();
        String sheetName = workbook.getSheetName(0);
        if (InvalidsExcelParser.setInvalidsINSheetName(resultDto, sheetName)){return null;}
        list.addAll(converterStoreGoDownDto(workbook.getSheetAt(0), resultDto,userId));
        setRemoveStoreGoDownDto(list);
        if(CollectionsUtil.isEmptyOrNullCollection(list)){
            List<String> listError = new ArrayList<>();
            listError.add("此模板为空模板，请填入对应数据后再批量导入，谢谢配合！");
            resultDto.setInvalids(listError);
            resultDto.setStoreGoDownDtos(null);
        }
        resultDto.setStoreGoDownDtos(list);
        return list;
    }
    private void setRemoveStoreGoDownDto(List<StoreGoDownDto> storeGoDownDto) {
        if(CollectionsUtil.isEmptyOrNullCollection(storeGoDownDto)){
            return;
        }
        for(int i=0;i<storeGoDownDto.size();i++){
            StoreGoDownDto dto = storeGoDownDto.get(i);
            List<BillInfoDto> billInfoDtoList = dto.getBillInfoDtoList();
            for(int j =0;j<billInfoDtoList.size();j++){
                BillInfoDto billInfoDto = billInfoDtoList.get(j);
                if(billInfoDto.getBillMedium()==null && billInfoDto.getBillType()==null && billInfoDto.getAcceptingCompanyType()==null){
                    storeGoDownDto.remove(dto);
                    i--;
                }
            }
        }
    }
    /**
     *
     *管理模式
     */
    public List<StoreGoDownDto> converterStoreGoDownDto(Sheet sheet, ExcelParserResultDto resultDto, String userId){
        List<StoreGoDownDto> list = new ArrayList<>();
        int i=1;
        for (Row row : sheet) {
            if (row.getRowNum() < 1) {
                continue;
            } //表格内容从第2行开始.
            StoreGoDownDto goDownDto = new StoreGoDownDto();
            List<BillInfoDto> billInfoDtoList = new ArrayList<>();
            BillInfoDto billInfoDto = new BillInfoDto();
            String billType = InvalidsExcelParser.getCellValue(row,0);
            InvalidsExcelParser.setInvalidsBillType(resultDto,i,row,0,billType);
            setBillMediumAndBillType(billInfoDto, billType);
            InvalidsExcelParser.setInvalidsBillNumber(billInfoDto,resultDto,i,row,1);
            InvalidsExcelParser.setInvalidsAmount(billInfoDto,resultDto,i,row,2);
            InvalidsExcelParser.setInvalidsDrawerName(billInfoDto,resultDto,i,row,3);
            InvalidsExcelParser.setInvalidsPayeeName(billInfoDto,resultDto,i,row,4);
            InvalidsExcelParser.setInvalidsAcceptingCompanyName(billInfoDto,resultDto,i,row,5);
            InvalidsExcelParser.setInvalidsAcceptingCompanyType(billInfoDto,resultDto,i,row,6);
            InvalidsExcelParser.setInvalidsBillStartDate(billInfoDto,resultDto,i,row,7);
            InvalidsExcelParser.setInvalidsBillDueDate(billInfoDto,resultDto,i,row,8);
            billInfoDto.setOperatorId(userId);
            billInfoDtoList.add(billInfoDto);
            goDownDto.setBillInfoDtoList(billInfoDtoList);
            InvalidsExcelParser.setInvalidsAjustDays(goDownDto,resultDto,i,row,9);
            InvalidsExcelParser.setInvalidsGoDownDate(goDownDto,resultDto,i,row,10);
            InvalidsExcelParser.setInvalidsGoDownType(goDownDto,resultDto,i,row,11);
            InvalidsExcelParser.setInvalidsGoDownPrice(goDownDto,resultDto,i,row,12);

            list.add(goDownDto);
            i++;
        }
        resultDto.setStoreGoDownDtos(list);
        return list;
    }

    private void setBillMediumAndBillType(BillInfoDto billInfoDto, String billType) {
        String babBkbELE = WEBBillType.ELE_BKB.getDisplayName();
        String babBkbPAP = WEBBillType.PAP_BKB.getDisplayName();
        if (babBkbELE.equals(billType) || babBkbPAP.equals(billType)) {
            if (babBkbELE.equals(billType)) {
                billInfoDto.setBillMedium(BABBillMedium.ELE);
            }
            if (babBkbPAP.equals(billType)) {
                billInfoDto.setBillMedium(BABBillMedium.PAP);
            }
            billInfoDto.setBillType(BABBillType.BKB);
        }
        String babCmbELE = WEBBillType.ELE_CMB.getDisplayName();
        String babCmbPAP = WEBBillType.PAP_CMB.getDisplayName();
        if (babCmbELE.equals(billType) || babCmbPAP.equals(billType)) {
            if (babCmbELE.equals(billType)) {
                billInfoDto.setBillMedium(BABBillMedium.ELE);
            }
            if (babCmbPAP.equals(billType)) {
                billInfoDto.setBillMedium(BABBillMedium.PAP);
            }
            billInfoDto.setBillType(BABBillType.CMB);
        }
    }

    /**
     * 解决新老版本兼容性,转成对应的Workbook实例
     */
    public Workbook createWorkbook(InputStream inp) {
        try {
            InputStream inputStream ;
            if (!inp.markSupported()) {
                inputStream= new PushbackInputStream(inp, 8);
            }else{
                inputStream = inp;
            }
            if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
                return new HSSFWorkbook(inp);
            }
            if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
                return new XSSFWorkbook(OPCPackage.open(inp));
            }
        } catch (Exception e) {
            throw new BusinessRuntimeException(BABExceptionCode.EXCEL_ERROR, e);
        }
        throw new BusinessRuntimeException(BABExceptionCode.EXCEL_ERROR, "excel版本目前poi无法解析!!!");
    }

    /**
     *获取excel的后缀 ，用以判断是新老版本
     */
    private String getExcelLastName(ExcelFileDto excelFileDto) {
        String lastName = null;
        try {
            String fileName = excelFileDto.getFileName();
            if(fileName.contains(".")){
                lastName = fileName.substring(fileName.indexOf(".")+1,fileName.length());
            }
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"文件格式错误，不是.xlsx或者.xls文件！",e);
        }
        return lastName;
    }
}
