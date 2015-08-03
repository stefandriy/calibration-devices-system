package com.softserve.edu.service.provider.buildGraphic;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;

import java.text.ParseException;
import java.util.*;

public class GraficBuilder {
    private static StringBuilder strBuild;

    public static List<MonthOfYear> listOfMonths(Date dateFrom, Date dateTo) throws ParseException {
        Calendar start = Calendar.getInstance();
        start.setTime(dateFrom);
        rollDateToFirstDayOfMonth(start);

        Calendar end = Calendar.getInstance();
        end.setTime(dateTo);
        rollDateToFirstDayOfMonth(end);

        List<MonthOfYear> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Date date = start.getTime(); start.before(end) || start.equals(end);
             start.add(Calendar.MONTH, 1), date = start.getTime()) {
            calendar.setTime(date);
            MonthOfYear item = new MonthOfYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            months.add(item);
        }
        return months;
    }

    public static List<ProviderEmployeeGrafic> builderData(List<Verification> verificationList,
                                                           List<MonthOfYear> months, List<User> listOfEmployee) throws ParseException {
        Map<String, ProviderEmployeeGrafic> employeeGraphicMap = new HashMap<>();

        for (Verification verification : verificationList) {
            Calendar expirDate = Calendar.getInstance();
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
            expirDate.setTime(verification.getSentToCalibratorDate());
            MonthOfYear item = new MonthOfYear(expirDate.get(Calendar.MONTH), expirDate.get(Calendar.YEAR));
            int indexOfMonth = months.indexOf(item);
            graphicItem.data[indexOfMonth]++;
        }
        List<ProviderEmployeeGrafic> graphicItemsList = listOfProviderEmployeeGrafic(employeeGraphicMap, listOfEmployee);

        return graphicItemsList;
    }


    public static List<ProviderEmployeeGrafic> listOfProviderEmployeeGrafic(Map<String,
            ProviderEmployeeGrafic> employeeGraphicMap, List<User> listOfEmployee) {
        List<ProviderEmployeeGrafic> graphicItemsList = new ArrayList<>();
        for (Map.Entry<String, ProviderEmployeeGrafic> item : employeeGraphicMap.entrySet()) {
            graphicItemsList.add(item.getValue());
        }
        for (ProviderEmployeeGrafic provEmp : graphicItemsList) {
            for (User user : listOfEmployee) {
                if (provEmp.name.equals(user.getUsername())) {
                    strBuild = new StringBuilder();
                    strBuild.append(user.getLastName()).append(" ");
                    strBuild.append(user.getFirstName()).append(" ");
                    strBuild.append(user.getMiddleName()).append(" ");
                    provEmp.name = strBuild.toString();
                }
            }
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