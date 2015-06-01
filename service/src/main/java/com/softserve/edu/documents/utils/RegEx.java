package com.softserve.edu.documents.utils;

/**
 * Regular expressions used during documents generations.
 */
public enum RegEx {
    FIND_ALL_COLUMNS("\\$(\\w+)"),
    FIND_ALL_FORMATTING_TOKENS("\\$|#");

    private String regEx;

    RegEx(String regEx) {
        this.regEx = regEx;
    }

    @Override
    public String toString() {
        return regEx;
    }
}
