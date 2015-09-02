package com.softserve.edu.dto;

/**
 * Created by vova on 02.09.15.
 */
public class NewOrganizationFilterSearch {

    private String id;
    private String name_admin;
    private String type_admin;
    private String email;
    private String phone_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_admin() {
        return name_admin;
    }

    public void setName_admin(String name_admin) {
        this.name_admin = name_admin;
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
