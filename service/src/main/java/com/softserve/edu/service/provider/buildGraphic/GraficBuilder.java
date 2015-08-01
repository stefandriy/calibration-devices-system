package com.softserve.edu.service.provider.buildGraphic;

import com.softserve.edu.entity.Verification;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.*;

public class GraficBuilder {

    public static List<MonthOfYear> listOfMonths(Date dateFrom, Date dateTo) throws ParseException {
        Calendar start = Calendar.getInstance();
        start.setTime(dateFrom);
        rollDateToFirstDayOfMonth(start);

        Calendar end = Calendar.getInstance();
        end.setTime(dateTo);
        rollDateToFirstDayOfMonth(end);

        List<MonthOfYear> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.MONTH, 1), date = start.getTime()) {
            calendar.setTime(date);
            MonthOfYear item = new MonthOfYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            months.add(item);
        }
        return months;
    }

    public static List<ProviderEmployeeGrafic> builderData(List<Verification> verificationList, List<MonthOfYear> months) throws ParseException {
        Map<String, ProviderEmployeeGrafic> employeeGraphicMap = new HashMap<>();
        Calendar initDateCal = Calendar.getInstance();

        for (Verification verification : verificationList) {
            ProviderEmployeeGrafic graphicItem;

            if (employeeGraphicMap.containsKey(verification.getProviderEmployee().getUsername())) {
                graphicItem = employeeGraphicMap.get(verification.getProviderEmployee().getUsername());
            } else {
                graphicItem = new ProviderEmployeeGrafic();
                graphicItem.monthList = months;
                graphicItem.data = new double[months.size()];
                graphicItem.name = verification.getProviderEmployee().getUsername();
                employeeGraphicMap.put(verification.getProviderEmployee().getUsername(), graphicItem);
            }
            initDateCal.setTime(verification.getInitialDate());
            MonthOfYear item = new MonthOfYear(initDateCal.get(Calendar.MONTH), initDateCal.get(Calendar.YEAR));
            int indexOfMonth = months.indexOf(item);
            graphicItem.data[indexOfMonth]++;
        }
        List<ProviderEmployeeGrafic> graphicItemsList = listOfProviderEmployeeGrafic(employeeGraphicMap);

        return graphicItemsList;
    }


    public static List<ProviderEmployeeGrafic> listOfProviderEmployeeGrafic(Map<String, ProviderEmployeeGrafic> employeeGraphicMap) {
        List<ProviderEmployeeGrafic> graphicItemsList = new ArrayList<>();
        for (Map.Entry<String, ProviderEmployeeGrafic> item : employeeGraphicMap.entrySet()) {
            graphicItemsList.add(item.getValue());
        }
        return graphicItemsList;
    }


    private static void rollDateToFirstDayOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}