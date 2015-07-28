package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String region;
    private String district;
    private String locality;
    private String street;
    private String building;
    private String flat;

    public Address() {}

    public Address(String region, String district, String locality, String street, String building,
                   String flat) {
        this.region = region;
        this.district = district;
        this.locality = locality;
        this.street = street;
        this.building = building;
        this.flat = flat;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("region", region)
                .append("district", district)
                .append("locality", locality)
                .append("street", street)
                .append("building", building)
                .append("flat", flat)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(region)
                .append(district)
                .append(locality)
                .append(street)
                .append(building)
                .append(flat)
                 .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Address) {
            final Address other = (Address) obj;
            return new EqualsBuilder()
                    .append(region, other.region)
                    .append(district, other.district)
                    .append(locality, other.locality)
                    .append(street, other.street)
                    .append(building, other.building)
                    .append(flat, other.flat)
                     .isEquals();
        } else {
            return false;
        }
    }
}
