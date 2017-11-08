package com.sumscope.bab.store.commons.util;
import java.util.ArrayList;
import java.util.List;

public final class SecurityStringUtil {
    private  static final String[] SPECIAL_CHARACTERS = new String[]{"'","‘","select","SELECT","having","HAVING","ltrim","LTRIM","(",")","=","|",":",";","and","AND"};

    private SecurityStringUtil(){}

    public static String validateStr(String param){
        String newParam = param;
        if(newParam == null){
            return null;
        }
        for(int i=0;i<SPECIAL_CHARACTERS.length;i++){
            if(newParam.contains(SPECIAL_CHARACTERS[i])){
                newParam= newParam.replace(SPECIAL_CHARACTERS[i],"");
            }
        }
        return newParam;
    }

    public static List<String> securityCopyListStr(List<String> arg){
        if(arg == null || arg.isEmpty()){
            return null;
        }
        List<String> target = new ArrayList<>();
        for(String src : arg){
            target.add(validateStr(src));
        }
         return target;
    }

    public static int compareUtils(int param){
       if(param > 0){
            return 1;
       }else if(param <= 0){
            return 0;
        }else{
            return -1;
        }
    }
}
