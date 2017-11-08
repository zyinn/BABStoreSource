package com.sumscope.bab.store.model.dto;

import com.sumscope.bab.store.commons.enums.WEBSearchParameterMode;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class AppInitialDataDto {
    /**
     * 某一查询条件的所有可选项目
     */
    private List<WEBParameterEnumDto> conditions;

    private String conditionName;

    private WEBSearchParameterMode conditionMode;

    public List<WEBParameterEnumDto> getConditions() {
        return conditions;
    }

    public void setConditions(List<WEBParameterEnumDto> conditions) {
        this.conditions = conditions;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public WEBSearchParameterMode getConditionMode() {
        return conditionMode;
    }

    public void setConditionMode(WEBSearchParameterMode conditionMode) {
        this.conditionMode = conditionMode;
    }
}
