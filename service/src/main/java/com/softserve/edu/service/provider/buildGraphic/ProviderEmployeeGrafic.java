package com.softserve.edu.service.provider.buildGraphic;

import java.util.List;

/**
 * Created by MAX on 01.08.2015.
 */
public class ProviderEmployeeGrafic {
    public String userName;
    public List<MonthOfYear> monthList;
    public int[] countOfWork;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<MonthOfYear> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<MonthOfYear> monthList) {
        this.monthList = monthList;
    }

    public int[] getCountOfWork() {
        return countOfWork;
    }

    public void setCountOfWork(int[] countOfWork) {
        this.countOfWork = countOfWork;
    }
}
