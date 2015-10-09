package com.softserve.edu.service.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nazarii Ivashkiv
 */
public class TypeConverter {

    /**
     * Converts set of enums to set of their string equivalents
     *
     * @param enums set witch need to be converted
     * @return set of string equivalents
     */
    public static Set<String> enumToString(Set<? extends Enum> enums) {
        Set<String> res = new HashSet<>();
        for (Enum anEnum : enums) {
            res.add(anEnum.name());
        }
        return res;
    }

    /**
     * Convert set of strings to set of their enum equivalents
     *
     * @param strings set witch need to be converted
     * @param clazz object of enum class to which need to be converted
     * @param <T> type of enum class to which need to be converted
     * @return set of converted enums
     */
    public static <T extends Enum<T>> Set<T> stringToEnum(Set<String> strings, Class<T> clazz) {
        Set<T> res = new HashSet<>();
        strings.stream()
                .forEach(s -> res.add(T.valueOf(clazz, s)));
        return res;
    }
}
