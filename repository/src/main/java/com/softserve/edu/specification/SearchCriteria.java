package com.softserve.edu.specification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria<T extends Enum<T>> {
    private String key;
    private String entityField;
    private String additionKey;
    private Operator operation;
    private ValueType valueType;
    private String joinEntityField;
    private Class<T> enumKeyType;

    /**
     * @param key
     * @param operation
     * @param enumKeyType
     * @param additionKey
     * @param valueType
     * @param joinEntityField
     */
    public SearchCriteria(String key, String entityField, Operator operation, Class<T> enumKeyType, String additionKey, ValueType valueType, String joinEntityField) {
        this.key = key;
        this.entityField = entityField;
        this.operation = operation;
        this.enumKeyType = enumKeyType;
        this.additionKey = additionKey;
        this.valueType = valueType;
        this.joinEntityField = joinEntityField;
    }

    /**
     * @param key
     * @param operation
     * @param enumKeyType
     */
    public SearchCriteria(String key, String entityField, Operator operation, Class<T> enumKeyType) {
        this(key, entityField, operation, enumKeyType, null, null, null);
    }

    /**
     * @param key
     * @param operation
     * @param valueType
     */
    public SearchCriteria(String key, String entityField, Operator operation, ValueType valueType) {
        this(key, entityField, operation, null, null, valueType, null);
    }

    public SearchCriteria(String key, String entityField, Operator operation, ValueType valueType, String joinEntityField) {
        this(key, entityField, operation, null, null, valueType, joinEntityField);
    }

    /**
     * @param key
     * @param operation
     * @param additionKey
     */
    public SearchCriteria(String key, String entityField, Operator operation, String additionKey) {
        this(key, entityField, operation, null, additionKey, null, null);
    }

    /**
     * @param key
     * @return
     */
    public T getEnum(String key) {
        return Enum.valueOf(enumKeyType, key);
    }

    public enum Operator {
        EQUAL, EQUAL_BY_ENUM, BETWEEN_DATE,  LIKE
    }

    public enum ValueType {
        STRING, LONG, INTEGER, BOOLEAN, DATE
    }

}
