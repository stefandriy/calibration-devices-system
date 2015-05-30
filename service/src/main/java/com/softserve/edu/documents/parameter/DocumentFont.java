package com.softserve.edu.documents.parameter;

/**
 * Available fonts.
 */
public enum DocumentFont {
    FREE_SERIF("FreeSerif");

    private String fontName;

    DocumentFont(String fontName) {
        this.fontName = fontName;
    }

    @Override
    public String toString() {
        return fontName + ".ttf";
    }
}
