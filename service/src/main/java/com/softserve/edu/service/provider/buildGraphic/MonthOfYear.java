package com.softserve.edu.service.provider.buildGraphic;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MonthOfYear {
    public int month;
    public int year;

    public MonthOfYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("month", month)
                .append("year", year)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(month)
                .append(year)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MonthOfYear) {
            final MonthOfYear other = (MonthOfYear) obj;
            return new EqualsBuilder()
                    .append(month, other.month)
                    .append(year, other.year)
                    .isEquals();
        } else {
            return false;
        }
    }
}