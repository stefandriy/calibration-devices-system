package com.softserve.edu.documents.document.meta;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads meta data of a document.
 */
public class MetaDataReader {
    private static Logger logger = Logger.getLogger(MetaDataReader.class);

    /**
     * Reads meta data of a document.
     *
     * @param document object of a class annotated as a Document
     * @return a map where each entry is has a column name as a key and the column
     * value as value
     */
    public Map<String, Object> getColumnsNamesValues(
            com.softserve.edu.documents.document.Document document) {

        Document documentAnnotation = document.getClass().getAnnotation(Document.class);

        Assert.notNull(documentAnnotation, "the specified object is not an " +
                "instance of a class annotated with the " +
                Document.class +
                " annotation");

        Method[] documentMethods = document.getClass().getMethods();
        Map<String, Object> columnMap = new HashMap<>();

        for (Method method : documentMethods) {
            Placeholder placeholder = method.getAnnotation(Placeholder.class);

            if (placeholder == null) {
                continue;
            }

            String columnName = placeholder.name();
            Object columnValue;

            try {
                columnValue = method.invoke(document);
            } catch (ReflectiveOperationException exceptionObject) {
                logger.error("exception while trying to invoke method " +
                        method.getName() + " of object " +
                        document.toString(), exceptionObject);
                throw new RuntimeException(exceptionObject);
            }

            columnMap.put(columnName, columnValue);
        }

        return columnMap;
    }
}
