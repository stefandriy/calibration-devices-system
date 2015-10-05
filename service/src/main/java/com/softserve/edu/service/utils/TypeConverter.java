package com.softserve.edu.service.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Назік on 10/1/2015.
 */
public class TypeConverter {
    public static Set<String> enumToString(Set<? extends Enum> enums) {
        Set<String> res = new HashSet<>();
        for (Enum anEnum : enums) {
            res.add(anEnum.name());

        }
        return res;
    }

    /**
     * TODO
     */
   /* public static <T extends Enum> Set<T> stringToEnum(Set<String> strings, Class<T> clazz) {
        Set<T> res = new HashSet<>();
        for (String string : strings) {
            // Object<> enu = ;
            res.add(clazz.cast(Enum.valueOf(clazz, string)));
        }
        return res;
    }*/
}
