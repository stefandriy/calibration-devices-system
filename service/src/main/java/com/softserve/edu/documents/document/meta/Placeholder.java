package com.softserve.edu.documents.document.meta;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method represents a document's column and
 * it's return value is the column's value. The method toString() is used
 * on the annotated method to get the column's value.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Placeholder {
    String name();
}
