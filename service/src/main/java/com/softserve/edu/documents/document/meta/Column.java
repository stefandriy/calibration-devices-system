package com.softserve.edu.documents.document.meta;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method is a document's column and it's return
 * value is the column's value.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Column {
    String name();
}
