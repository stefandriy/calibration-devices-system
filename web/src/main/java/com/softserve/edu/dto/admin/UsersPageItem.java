package com.softserve.edu.dto.admin;

import java.util.List;

public class UsersPageItem {

    private String username;
   // private List<String> roles;
    private String password;
    private String role;

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String secondPhone;
    private Boolean isAvaliable;

    private String organization;
    private Long countOfVerification;
    private Long calibratorTasks;
    private Long stateVerificatorTasks;

    public UsersPageItem() {
    }

    public UsersPageItem(String username, List<String> roles, String firstName, String lastName, String middleName, String email, String phone) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String someRole : roles) {
            stringBuilder.append(someRole).append(" ");
        }
        role = stringBuilder.toString().trim();

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    

    public String getSecondPhone() {
		return secondPhone;
	}

	public void setSecondPhone(String secondPhone) {
		this.secondPhone = secondPhone;
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

    public Long getCountOfVerification() {
        return countOfVerification;
    }

    public Long getStateVerificatorTasks() {
        return stateVerificatorTasks;
    }

    public void setStateVerificatorTasks(Long stateVerificatorTasks) {
        this.stateVerificatorTasks = stateVerificatorTasks;
    }

    public void setCountOfVerification(Long countOfVerification) {
        this.countOfVerification = countOfVerification;
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
                         String middleName, String phone, String secondPhone, String organization,
                         Long countOfVerification, Long calibratorTasks, Long stateVerificatorTasks, Boolean isAvaliable) {

        this.username = username;

        StringBuilder stringBuilder = new StringBuilder();
        for (String someRole : roles) {
            stringBuilder.append(someRole).append(" ");
        }
        role = stringBuilder.toString().trim();

        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.secondPhone = secondPhone;
        this.organization = organization;
        this.countOfVerification = countOfVerification;
        this.calibratorTasks = calibratorTasks;
        this.stateVerificatorTasks = stateVerificatorTasks;
        this.isAvaliable = isAvaliable;
    }


    @Override
    public String toString() {
        return "UsersPageItem{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", secondPhone='" + secondPhone + '\'' +
                ", isAvaliable=" + isAvaliable +
                ", organization='" + organization + '\'' +
                ", countOfVerification=" + countOfVerification +
                '}';
    }

}
