package com.softserve.edu.documents.size;

/**
 * Contains typographic unit and their properties.
 * These units can be used for document and text sizes.
 */
public enum SizeUnit {
    /**
     * Centimeter.
     */
    CM(566.929133858),
    /**
     * Typographic point.
     */
    PT(20d),
    /**
     * Typographic twip.
     * It's the smallest typographic measurement used in this application.
     */
    TWIP(1d);

    /**
     * Specifies how many twips is equal to 1 current typographic unit.
     */
    final private Double inTwips;

    /**
     * Constructor.
     *
     * @param inTwips how many twips is equal to 1 current typographic unit
     *                that is being created.
     */
    SizeUnit(Double inTwips) {
        this.inTwips = inTwips;
    }

    public Double getInTwips() {
        return inTwips;
    }
}
