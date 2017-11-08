package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.store.commons.enums.BABSheetName;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.ExcelParserResultDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/22.
 * excel各个字段验证类
 */
public final class InvalidsExcelParser {

    private InvalidsExcelParser() {
    }

    public static boolean setInvalidsINSheetName(ExcelParserResultDto resultDto, String sheetName) {
        String storeBKBIn= BABSheetName.BAB_STORE_BKB_IN_SHEET_NAME.getDisplayName();
        String storeCMBIn= BABSheetName.BAB_STORE_CBM_IN_SHEET_NAME.getDisplayName();
        if (invalidsSheetName(resultDto, sheetName,storeBKBIn,storeCMBIn)){ return true;}
        return false;
    }

    private static String getErrorMsg(int i, int j, String msg) {
        String template = "第%d行，第%d列%s错误！";
        return String.format(template, i, (j+1+1), msg);//因web端新增一列序号，为保持一致，报错为j+1
    }
    private static void setInvalidsMsg(ExcelParserResultDto resultDto, String e) {
        if (resultDto.getInvalids() != null && resultDto.getInvalids().size() > 0) {
            resultDto.getInvalids().add(e);
        } else {
            List<String> invalidsList = new ArrayList<>();
            invalidsList.add(e);
            resultDto.setInvalids(invalidsList);
        }
    }

    /**
     *判断模板是否正确
     */
    public static boolean invalidsSheetName(ExcelParserResultDto resultDto, String sheetName,String storeName,String storeName2) {
        if(!storeName.equals(sheetName) && !storeName2.equals(sheetName)){
            List<String> invalidsList = new ArrayList<>();
            String invalids = "模板错误,该模板不是"+storeName+"模板!";
            invalidsList.add(invalids);
            resultDto.setInvalids(invalidsList);
            return true;
        }
        return false;
    }

    /**
     *验证票类
     */
    public static String setInvalidsBillType(ExcelParserResultDto resultDto, int i, Row row,int l,String billType) {
        try {
            billType = row.getCell(l)!=null ? row.getCell(l).getStringCellValue().trim() : null;
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"票类"));
        }
        return billType;
    }

    /**
     *验证票号
     */
    public static void setInvalidsBillNumber(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setBillNumber(InvalidsExcelParser.getCellValue(row,l));
            billTypeFormatValidate(billInfoDto,resultDto,i,l);
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"票号"));
        }
    }

    /**
     *  验证四种票据格式
     *  电银：30位，第1位为1
     *	纸银：16位，第7位为5
     *	纸商：16位，第7位为6
     *	电商：30位，第1位为2
     */
    private static void billTypeFormatValidate(BillInfoDto infoDto, ExcelParserResultDto resultDto, int i,int l){
        validateBillNumber(resultDto,infoDto, BABBillMedium.ELE, BABBillType.BKB,"^1[0-9]{29}$","电银格式：30位，第1位为1",i,l);
        validateBillNumber(resultDto,infoDto,BABBillMedium.ELE,BABBillType.CMB,"^2[0-9]{29}$","电商格式：30位，第1位为2",i,l);
        validateBillNumber(resultDto,infoDto,BABBillMedium.PAP,BABBillType.BKB,"^[0-9]{6}5[0-9]{9}$","纸银格式：16位，第7位为5",i,l);
        validateBillNumber(resultDto,infoDto,BABBillMedium.PAP,BABBillType.CMB,"^[0-9]{6}6[0-9]{9}$","纸商格式：16位，第7位为6",i,l);
    }

    private static void validateBillNumber( ExcelParserResultDto resultDto,BillInfoDto infoDto, WEBEnum billMedium, DatabaseEnum billType,
                                            String pattern, String msg, int i,int l) {
        if(infoDto.getBillMedium()== billMedium && infoDto.getBillType()==billType) {
            if (!Pattern.compile(pattern).matcher(infoDto.getBillNumber()).matches()){
                setInvalidsMsg(resultDto,getErrorMsg(i,l,msg));
            }
            infoDto.setBillNumber(infoDto.getBillNumber());
        }
    }
    /**
     *验证票面金额
     */
    public static void setInvalidsAmount(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setAmount(InvalidsExcelParser.getCellValue(row,l)!=null ? new BigDecimal(InvalidsExcelParser.getCellValue(row,l)) : null);
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"票面金额"));
        }
    }

    /**
     *验证出票人
     */
    public static void setInvalidsDrawerName(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setDrawerName(InvalidsExcelParser.getCellValue(row,l));
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"出票人"));
        }
    }

    /**
     *验证承兑方
     */
    public static void setInvalidsAcceptingCompanyName(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setAcceptingCompanyName(InvalidsExcelParser.getCellValue(row,l));
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"承兑方"));
        }
    }

    /**
     *承兑方类别
     */
    public static void setInvalidsAcceptingCompanyType(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setAcceptingCompanyType(getBABAcceptingCompanyType(InvalidsExcelParser.getCellValue(row,l)));
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"承兑方类别"));
        }
    }

    /**
     *验证出票日
     */
    public static void setInvalidsBillStartDate(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            if(row.getCell(l)!=null){
                Date dateCellValue = row.getCell(l).getDateCellValue();
                billInfoDto.setBillStartDate(invalidsTimes(dateCellValue,resultDto,i,l,"出票日"));
            }
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"出票日"));
            billInfoDto.setBillStartDate(null);
        }
    }
    private static Date invalidsTimes(Date date, ExcelParserResultDto resultDto, int i, int l, String msg){
        if(date!=null){
            String time = String.valueOf(date.getTime());
            if(time.length()!=13){
                setInvalidsMsg(resultDto,getErrorMsg(i,l,msg));
                return null;
            }
            return date;
        }
       return null;
    }
    /**
     *验证到期日
     */
    public static void setInvalidsBillDueDate(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            if(row.getCell(l)!=null){
                Date dateCellValue = row.getCell(l).getDateCellValue();
                billInfoDto.setBillDueDate(invalidsTimes(dateCellValue,resultDto,i,l,"到期日"));
            }
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"到期日"));
            billInfoDto.setBillDueDate(null);
        }
    }

    /**
     *验证入库日期
     */
    public static void setInvalidsGoDownDate(StoreGoDownDto goDownDto,ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            if(row.getCell(l)!=null){
                Date dateCellValue = row.getCell(l).getDateCellValue();
                goDownDto.setGodownDate( invalidsTimes(dateCellValue,resultDto,i,l,"入库日期"));
            }
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"入库日期"));
            goDownDto.setGodownDate(null);
        }
    }
    /**
     *验证入库类型
     */
    public static void setInvalidsGoDownType(StoreGoDownDto goDownDto,ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            goDownDto.setGodownType(getBABStoreGoDownType(InvalidsExcelParser.getCellValue(row,l)));
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"入库类型"));
        }
    }

    /**
     *验证入库价格
     */
    public static void setInvalidsGoDownPrice(StoreGoDownDto goDownDto,ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            if(InvalidsExcelParser.getCellValue(row,l)!=null){
                goDownDto.setGodownPrice(new BigDecimal(InvalidsExcelParser.getCellValue(row,l)));
            }
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"入库价格"));
        }
    }

    public static void setInvalidsAjustDays(StoreGoDownDto goDownDto,ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            if(row.getCell(l)!=null){
                goDownDto.setAdjustDays((int)(row.getCell(l).getNumericCellValue()));
            }
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"调整天数"));
            goDownDto.setAdjustDays(null);
        }
    }
    public static void setInvalidsPayeeName(BillInfoDto billInfoDto, ExcelParserResultDto resultDto, int i, Row row, int l) {
        try {
            billInfoDto.setPayeeName(InvalidsExcelParser.getCellValue(row,l));
        } catch (Exception e) {
            setInvalidsMsg(resultDto,getErrorMsg(i,l,"收款人"));
        }
    }

    private static BABStoreGoDownType getBABStoreGoDownType(String companyTypeName){
        for(BABStoreGoDownType type: BABStoreGoDownType.values()){
            if(type.getDisplayName().equals(companyTypeName)){
                return type;
            }
        }
        return null;
    }

    private static BABAcceptingCompanyType getBABAcceptingCompanyType(String companyTypeName){
        for(BABAcceptingCompanyType type: BABAcceptingCompanyType.values()){
            if(type.getDisplayName().equals(companyTypeName)){
                return type;
            }
        }
        return null;
    }

    public static String getCellValue( Row row,int i) {
        Cell cell=row.getCell(i);
        if(cell!=null){
            if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                short format = cell.getCellStyle().getDataFormat();
                SimpleDateFormat sdf = null;
                if(format == 14 || format == 31 || format == 57 || format == 58){
                    //日期
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }else if (format == 20 || format == 32) {
                    //时间
                    sdf = new SimpleDateFormat("HH:mm");
                }else{
                    //数字
                    return String.valueOf(row.getCell(i).getNumericCellValue());
                }
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                return sdf.format(date);
            }else {
                return String.valueOf(cell.getStringCellValue());
            }
        }else{
            return null;
        }
    }

}
