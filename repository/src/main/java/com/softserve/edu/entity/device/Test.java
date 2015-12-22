package com.softserve.edu.entity.device;

import com.softserve.edu.common.Constants;

import java.text.SimpleDateFormat;

/**
 * Created by Sonka on 22.12.2015.
 */
public class Test {
    public static void main (String[] args){

        String id = (new SimpleDateFormat(Constants.DAY_MONTH_YEAR).format(22-12-2015)).toString()+1+String.format("%04d", 1);
        long lid = Long.parseLong(id);
        System.out.print(lid);
    }
}
