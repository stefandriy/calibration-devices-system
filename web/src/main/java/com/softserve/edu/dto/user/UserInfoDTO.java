package com.softserve.edu.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoDTO {
    private String username;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String phone;
    private String secondPhone;
    private long numberOfVerifications;
    private boolean isAvailable;

    public UserInfoDTO(String username, List<String> roles, String firstName, String lastName, String phone, String secondPhone, long numberOfVerifications, boolean isAvailable) {
        this.username = username;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.secondPhone = secondPhone;
        this.numberOfVerifications = numberOfVerifications;
        this.isAvailable = isAvailable;
    }
}
