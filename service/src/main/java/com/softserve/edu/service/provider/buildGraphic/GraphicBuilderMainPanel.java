package com.softserve.edu.service.provider.buildGraphic;

/**
 * Created by Володя on 08.08.2015.
 */

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;

import java.text.ParseException;
import java.util.*;

public class GraphicBuilderMainPanel {
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

    public static List<ProviderEmployeeGraphic> builderData(List<Verification> verificationList,
                                                           List<MonthOfYear> months, Organization organization) throws ParseException {
        Map<String, ProviderEmployeeGraphic> employeeGraphicMap = new HashMap<>();

        for (Verification verification : verificationList) {
            Calendar expirDate = Calendar.getInstance();
            ProviderEmployeeGraphic graphicItem;

            if (employeeGraphicMap.containsKey(organization.getName())) {
                graphicItem = employeeGraphicMap.get(organization.getName());
            } else {
                graphicItem = new ProviderEmployeeGraphic();
                graphicItem.monthList = months;
                graphicItem.data = new double[months.size()];
                graphicItem.name = organization.getName();
                employeeGraphicMap.put(organization.getName(), graphicItem);
            }
            expirDate.setTime(verification.getInitialDate());
            MonthOfYear item = new MonthOfYear(expirDate.get(Calendar.MONTH), expirDate.get(Calendar.YEAR));
            int indexOfMonth = months.indexOf(item);
            graphicItem.data[indexOfMonth]++;
        }
        List<ProviderEmployeeGraphic> graphicItemsList = listOfProviderEmployeeGrafic(employeeGraphicMap, organization);

        return graphicItemsList;
    }


    public static List<ProviderEmployeeGraphic> listOfProviderEmployeeGrafic(Map<String,
            ProviderEmployeeGraphic> employeeGraphicMap, Organization organization) {
        List<ProviderEmployeeGraphic> graphicItemsList = new ArrayList<>();
        for (Map.Entry<String, ProviderEmployeeGraphic> item : employeeGraphicMap.entrySet()) {
            graphicItemsList.add(item.getValue());
        }
        for (ProviderEmployeeGraphic provEmp : graphicItemsList) {
                    provEmp.name = organization.getName();
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
