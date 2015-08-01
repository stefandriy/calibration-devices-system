package com.softserve.edu.service.provider.buildGraphic;

import java.util.List;

/**
 * Created by MAX on 01.08.2015.
 */
public class ProviderEmployeeGrafic {
    public String name;
    public List<MonthOfYear> monthList;
    public double[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public List<MonthOfYear> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<MonthOfYear> monthList) {
        this.monthList = monthList;
    }

}


