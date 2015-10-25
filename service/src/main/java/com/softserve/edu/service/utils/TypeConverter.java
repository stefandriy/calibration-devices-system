package com.softserve.edu.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


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
    public static <T extends Enum> Set<String> enumToString(Set<T> enums) {

        return enums.stream().map(Enum::name).collect(Collectors.toSet());
    }

    /**
     * Convert set of strings to set of their enum equivalents
     *
     * @param strings set witch need to be converted
     * @param clazz   object of enum class to which need to be converted
     * @param <T>     type of enum class to which need to be converted
     * @return set of converted enums
     */
    public static <T extends Enum<T>> Set<T> stringToEnum(Set<String> strings, Class<T> clazz) {
        Set<T> res = new HashSet<>();
        strings.stream()
                .forEach(s -> res.add(T.valueOf(clazz, s)));
        return res;
    }

    public static <T> Map<String, String> ObjectToMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        //noinspection unchecked
        Map<String, String> map = objectMapper.convertValue(object, Map.class);
        return  map.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /*public static <T> Map<String, Object> ObjectToMap(Object object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> objectAsMap = new HashMap<>();
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null)
                objectAsMap.put(pd.getName(), reader.invoke(object));
        }
        return objectAsMap;
    }*/

}
