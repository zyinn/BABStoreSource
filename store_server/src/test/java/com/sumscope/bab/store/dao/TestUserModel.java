package com.sumscope.bab.store.dao;

import java.math.BigDecimal;

/**
 * Created by fan.bai on 2017/3/23.
 * 测试用数据模型
 */
public class TestUserModel {
    private String id;
    private String name;
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
