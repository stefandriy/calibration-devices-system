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

    private String organization;
    private Long countOfVarification;

    public UsersPageItem() {}

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
        this.roles = roles;
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



    public UsersPageItem(String username, List<String> roles, String firstName, String lastName,
                         String middleName,String phone, String organization,
                         Long countOfVarification) {
        this.username = username;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.organization = organization;
        this.countOfVarification = countOfVarification;
    }
    public UsersPageItem(String username, String role, String firstName, String lastName,
                         String middleName,String phone, String organization,
                         Long countOfVarification) {
        this.username = username;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.organization = organization;
        this.countOfVarification = countOfVarification;
    }
    @Override
    public String toString() {
        return "UsersPageItem{" +
                "username='" + username + '\'' +
                ", roles='" + roles + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                ", countOfVarification=" + countOfVarification +
                '}';
    }
}
