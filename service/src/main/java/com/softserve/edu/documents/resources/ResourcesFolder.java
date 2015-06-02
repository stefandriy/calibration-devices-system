package com.softserve.edu.documents.resources;

/**
 * Constants names of standard resources folders.
 */
public enum ResourcesFolder {
    DOCUMENTS_TEMPLATES {
        @Override
        public String toString() {
            return "documentsTemplates";
        }
    },
    FONTS {
        @Override
        public String toString() {
            return "fonts";
        }
    }
}
