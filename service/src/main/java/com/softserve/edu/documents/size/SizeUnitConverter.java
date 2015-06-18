package com.softserve.edu.documents.size;

import org.springframework.util.Assert;

/**
 * Converter for size units.
 */
public class SizeUnitConverter {
    /**
     * Converts a value in one type of size units into a value of a different
     * size units.
     *
     * @param value            a value to convert
     * @param originalSizeUnit the value's size units
     * @param resultSizeUnit   size units of the resulting value
     * @return the converted value
     */
    public static Double convert(double value, SizeUnit originalSizeUnit,
                                 SizeUnit resultSizeUnit) {
        Assert.notNull(originalSizeUnit, SizeUnit.class.getSimpleName() + "can't" +
                "be null");
        Assert.notNull(resultSizeUnit, SizeUnit.class.getSimpleName() + "can't" +
                "be null");

        double valueInTwips = value * originalSizeUnit.getInTwips();
        return valueInTwips / resultSizeUnit.getInTwips();
    }
}
