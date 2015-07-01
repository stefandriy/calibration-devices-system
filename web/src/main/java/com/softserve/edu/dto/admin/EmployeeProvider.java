package com.softserve.edu.dto.admin;

import com.softserve.edu.entity.user.ProviderEmployee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAX on 29.06.2015.
 */
public class EmployeeProvider {

    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private String role;


    public EmployeeProvider() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public EmployeeProvider(String username, String firstName, String lastName, String middleName, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.role = role;
    }

    @Override
    public String toString() {
        return "EmployeeProvider{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                '}';

    }

    public static List<EmployeeProvider> giveListOfProvidors(List<ProviderEmployee> list) {
        List<EmployeeProvider> listEmployee = new ArrayList<>();
        for (ProviderEmployee providEmployee : list) {
            listEmployee.add(new EmployeeProvider(providEmployee.getUsername(), providEmployee.getFirstName(), providEmployee.getLastName(), providEmployee.getMiddleName(), providEmployee.getRole()));
        }
        return listEmployee;
    }
}
