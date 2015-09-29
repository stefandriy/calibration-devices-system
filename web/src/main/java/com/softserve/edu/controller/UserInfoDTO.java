package com.softserve.edu.controller;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
    private String username;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String phone;
    private Long numberOfVerifications;

    public UserInfoDTO(String username, List<String> roles, String firstName, String lastName, String phone, Long numberOfVerifications) {
        this.username = username;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.numberOfVerifications = numberOfVerifications;
    }
}
