package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;

/**
 * Document that contains the meteorological requirements for testing a device
 */
@Embeddable
public class MeteorologicalDocument {
    private String name;
    private String sign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("sign", sign)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(name)
                .append(sign)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof MeteorologicalDocument){
            final MeteorologicalDocument other = (MeteorologicalDocument) obj;
            return new EqualsBuilder()
                    .append(name, other.name)
                    .append(sign, other.sign)
                    .isEquals();
        } else {
            return false;
        }
    }
}
