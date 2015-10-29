package com.softserve.edu.dto;


public class NewOrganizationFilterSearch {

    private String id;
    private String name;
    private String type_admin;
    private String email;
    private String phone_number;
    private String street;
    private String region;
    private String district;
    private String locality;

    public NewOrganizationFilterSearch (){}

    public NewOrganizationFilterSearch(String id, String name, String type_admin, String email, String phone_number, String street, String region, String district, String locality) {
        this.id = id;
        this.name = name;
        this.type_admin = type_admin;
        this.email = email;
        this.phone_number = phone_number;
        this.street = street;
        this.region = region;
        this.district = district;
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_admin() {
        return type_admin;
    }

    public void setType_admin(String type_admin) {
        this.type_admin = type_admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
