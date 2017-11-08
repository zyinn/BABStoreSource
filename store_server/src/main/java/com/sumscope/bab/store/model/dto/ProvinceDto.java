package com.sumscope.bab.store.model.dto;

public class ProvinceDto extends WEBParameterEnumDto {
    private String pinyin;

    private String pinyinFull;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFull() {
        return pinyinFull;
    }

    public void setPinyinFull(String pinyinFull) {
        this.pinyinFull = pinyinFull;
    }
}
