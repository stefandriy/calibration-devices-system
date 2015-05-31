package com.softserve.edu.documents.utils;

import java.util.Collection;

/**
 * Created by Oleg on 5/31/2015.
 */
public class RegEx {
    public static String findAllColumns() {
        return "\\$(\\w+)";
    }

    public static String findAllFormattingTokens() {
        return "\\$|#";
    }
}
