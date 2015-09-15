package com.softserve.edu.service.utils;

/**
 * Created by vova on 15.09.15.
 */
public class OrganizationAdminDTO {

    private String firstName;
    private String lastName;
    private String middleName;

    public OrganizationAdminDTO() {
    }

    public OrganizationAdminDTO(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
