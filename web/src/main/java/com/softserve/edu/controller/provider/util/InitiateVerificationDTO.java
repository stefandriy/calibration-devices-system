package com.softserve.edu.controller.provider.util;

/**
 * Created by Oles Onyshchak on 5/29/2015.
 */
public class InitiateVerificationDTO {
    private String surname;
    private String name;
    private String middleName;
    private String phone;
    private String region;
    private String district;
    private String locality;
    private String street;
    private String flat;
    private String calibratorName;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getCalibratorName() {
        return calibratorName;
    }

    public void setCalibratorName(String calibratorName) {
        this.calibratorName = calibratorName;
    }

    @Override
    public String toString() {
        return "InitiateVerificationDTO{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", locality='" + locality + '\'' +
                ", street='" + street + '\'' +
                ", flat='" + flat + '\'' +
                ", calibratorName='" + calibratorName + '\'' +
                '}';
    }
}
