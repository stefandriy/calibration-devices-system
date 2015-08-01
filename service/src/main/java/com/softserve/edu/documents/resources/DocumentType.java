package com.softserve.edu.documents.resources;

/**
 * Contains types of documents supported by the application.
 */
public enum DocumentType {
    VERIFICATION_CERTIFICATE,
    UNFITNESS_CERTIFICATE,
    INFO_DOCUMENT;

    /**
     * @return the template's name with format
     */
    @Override
    public String toString() {
        return name().toLowerCase() + ".docx";
    }

}