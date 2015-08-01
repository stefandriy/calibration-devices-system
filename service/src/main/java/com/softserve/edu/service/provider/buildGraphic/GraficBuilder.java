package com.softserve.edu.service.provider.buildGraphic;


import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GraficBuilder {
    Logger logger = Logger.getLogger(GraficBuilder.class);

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static List<ProviderEmployeeGrafic> builder(Date dateFrom,Date dateTo,List<Verification> verificationList, List<User> userList) throws ParseException {


        Calendar start = Calendar.getInstance();
        start.setTime(dateFrom);
        rollDateToFirstDayOfMonth(start);

        Calendar end = Calendar.getInstance();
        end.setTime(dateTo);
        rollDateToFirstDayOfMonth(end);

        List<MonthOfYear> months = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.MONTH, 1), date = start.getTime()) {
            cal.setTime(date);
            MonthOfYear item = new MonthOfYear(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
            months.add(item);
        }

        Map<String, ProviderEmployeeGrafic> employeeGraphicMap = new HashMap<>();
        Calendar initDateCal = Calendar.getInstance();


        for (Verification v : verificationList) {
            ProviderEmployeeGrafic graphicItem;

            if (employeeGraphicMap.containsKey(v.getProviderEmployee().getUsername())) {
                graphicItem = employeeGraphicMap.get(v.getProviderEmployee().getUsername());
            } else {
                graphicItem = new ProviderEmployeeGrafic();
                graphicItem.monthList = months;
                graphicItem.countOfWork = new int[months.size()];
                graphicItem.userName = v.getProviderEmployee().getUsername();
                employeeGraphicMap.put(v.getProviderEmployee().getUsername(), graphicItem);
            }

            initDateCal.setTime(v.getInitialDate());
            MonthOfYear item = new MonthOfYear(initDateCal.get(Calendar.MONTH), initDateCal.get(Calendar.YEAR));
            int indexOfMonth = months.indexOf(item);
            graphicItem.countOfWork[indexOfMonth]++;
        }

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