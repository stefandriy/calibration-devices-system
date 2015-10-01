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
    /*public static Set<? extends Enum> stringToEnum(Set<String> strings, Class<? extends Enum> clazz) {
        Set<? extends Enum> res = new HashSet<>();
        for (String string : strings) {
            res.add(Enum.valueOf(string));
        }
        return res;
    }*/
}
