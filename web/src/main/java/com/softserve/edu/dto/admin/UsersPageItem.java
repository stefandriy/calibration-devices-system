package com.softserve.edu.dto.admin;

import java.util.List;

public class UsersPageItem {

    private String username;
    private List<String> roles;
    private String password;
    private String role;

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private Boolean isAvaliable;

    private String organization;
    private Long countOfVarification;
    private Long calibratorTasks;

    public UsersPageItem() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        if (roles.size() == 1) {
            this.role = roles.get(0);
        } else {
            for (int i = 0; i < roles.size(); i++) {
                this.role =(role==null)?roles.get(i): role+ " "+roles.get(i);
            }

        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Long getCountOfVarification() {
        return countOfVarification;
    }

    public void setCountOfVarification(Long countOfVarification) {
        this.countOfVarification = countOfVarification;
    }


    public Long getCalibratorTasks() {
        return calibratorTasks;
    }

    public void setCalibratorTasks(Long calibratorTasks) {
        this.calibratorTasks = calibratorTasks;
    }
    public Boolean getIsAvaliable() {
        return isAvaliable;
    }

    public void setIsAvaliable(Boolean isAvaliable) {
        this.isAvaliable = isAvaliable;

    }

    public UsersPageItem(String username, List<String> roles, String firstName, String lastName,
                         String middleName, String phone, String organization,
                         Long countOfVarification,Long calibratorTasks, Boolean isAvaliable) {

        this.username = username;
        if (roles.size() == 1) {
            role = roles.get(0);
        } else {
            for (int i = 0; i < roles.size(); i++) {
                role =(role==null)?roles.get(i): role+ " "+roles.get(i);
            }

        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.organization = organization;
        this.countOfVarification = countOfVarification;
        this.calibratorTasks=calibratorTasks;
        this.isAvaliable = isAvaliable;
    }


    @Override
    public String toString() {
        return "UsersPageItem{" +
                "username='" + username + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isAvaliable=" + isAvaliable +
                ", organization='" + organization + '\'' +
                ", countOfVarification=" + countOfVarification +
                '}';
    }

}
