package com.softserve.edu.dto.admin;

/**
 * Created by vova on 03.09.15.
 */
public class OrganizationPageDTO {

        private Long id;
        private String name;
        private String email;
        private String phone;
        private String types;
        private String[] arrayTypes;
        private String district;
        private String locality;
        private String street;
        private String region;

    public OrganizationPageDTO(){}

    public OrganizationPageDTO(Long id, String name, String email, String types, String phone, String region, String locality, String district,  String street ,String[] arrayTypes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.types = types;
        this.phone = phone;
        this.region = region;
        this.locality = locality;
        this.district = district;
        this.street = street;
        this.arrayTypes = arrayTypes;
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

    public String getName() {
            return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String[] getArrayTypes() {
        return arrayTypes;
    }

    public void setArrayTypes(String[] arrayTypes) {
        this.arrayTypes = arrayTypes;
    }
}

