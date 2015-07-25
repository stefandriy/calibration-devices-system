package com.softserve.edu.service.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MAX on 21.07.2015.
 */
public class TransformStringsToMonths {

    public List<String> transferToMonthArray(String dataFrom, String dataTo) {

        int from = parser(dataFrom, dataTo)[0];
        int to = parser(dataFrom, dataTo)[1];
        List list = new ArrayList();
        if (to >= from) {
            for (int i = from; i < to + 1; i++) {
                list.add(getMonth(i));
            }
        } else {
            for (int i = from; i < 13; i++) {
                list.add(getMonth(i));
            }
            for (int i = 1; i < to + 1; i++) {
                list.add(getMonth(i));
            }
        }
        return list;
    }


    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public int[] parser(String dataFrom, String dataTo) {
        int arr[] = new int[2];
        String fromDate = (String) dataFrom.subSequence(3, 5);
        String toDate = (String) dataTo.subSequence(3, 5);
        int from = Integer.valueOf(fromDate);
        int to = Integer.valueOf(toDate);
        arr[0] = from;
        arr[1] = to;
        return arr;

    }

    public String getQueryproviderUsername() {
        String providerUsername = "SELECT distinct providerEmployee_username as username FROM verification " +
                " where provider_organizationId= ?1 and  providerEmployee_username is not null";
        return providerUsername;
    }

    public String getQuerytoGrafic() {
        String toGrafic = "select  count(v.providerEmployee_username) as data, month(initialDate) as months" +
                " from verification v  " +
                "  where v.providerEmployee_username= ?1 " +
                " and  initialDate Between ?2 and ?3 " +
                " group by month(initialDate) ";
        return toGrafic;
    }


    public Date convertToData(String date) throws ParseException {
        Date newDate = null;
        if (!(date.equalsIgnoreCase("null"))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                newDate = dateFormat.parse(date);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(), 75);
            }
        }
        return newDate;
    }

    public  List<Double> identifyProviderEmployee(int[]arr,List<Object[]> list ){
        List<Double> countOfWork  = new ArrayList<>();
        double iterat = 0.0;
        double d = 0.0;
        for (int i = arr[0]; i < arr[1] + 1; i++) {
            boolean avaible = false;

            if (list.size() == 0) {
                countOfWork.add(0.0);
                avaible = true;
            }

            for (int j = 0; j < list.size(); j++) {
                iterat = Integer.valueOf(String.valueOf(list.get(j)[1]));
                if (i == iterat) {
                    d = Double.valueOf(String.valueOf(list.get(j)[0]));
                    countOfWork.add(d);
                    avaible = true;
                    break;
                }
            }
            if (!avaible) {
                countOfWork.add(0.0);
            }
        }
        return countOfWork;
    }

}

