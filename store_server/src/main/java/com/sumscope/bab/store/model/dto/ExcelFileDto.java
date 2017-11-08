package com.sumscope.bab.store.model.dto;

/**
 * Excel文件传递Dto
 */
public class ExcelFileDto {

    /**
     * 文件内容
     */
    private String data;

    /**
     * 文件名
     */
    private String fileName;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

