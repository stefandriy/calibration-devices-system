package com.softserve.edu.service.utils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAX on 21.07.2015.
 */
public class TransformStringsToMonths {
    public static List<String> transferToMonthArray(String dataFrom, String dataTo) {
        String fromDate = (String) dataFrom.subSequence(3, 5);
        String toDate = (String) dataTo.subSequence(3, 5);
        int from = Integer.valueOf(fromDate);
        int to = Integer.valueOf(toDate);
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


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

}

