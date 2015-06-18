package com.softserve.edu.documents.document.meta;

import java.lang.annotation.*;

/**
 * Indicates that the annotated class is a document type.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Document {
}