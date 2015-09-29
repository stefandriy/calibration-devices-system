package com.softserve.edu.entity.util;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.user.User;


public class AddEmployeeBuilderNew {
    public String firstName;
    public String lastName;
    public String middleName;
    public String phone;
    public String email;
    public String username;
    public String password;
    public Address address;
    public Boolean isAvailable;

    public AddEmployeeBuilderNew firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AddEmployeeBuilderNew lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AddEmployeeBuilderNew middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public AddEmployeeBuilderNew phone(String phone) {
        this.phone = phone;
        return this;
    }

    public AddEmployeeBuilderNew email(String email) {
        this.email = email;
        return this;
    }

    public AddEmployeeBuilderNew username(String username) {
        this.username = username;
        return this;
    }

    public AddEmployeeBuilderNew password(String password) {
        this.password = password;
        return this;
    }

    public AddEmployeeBuilderNew address(Address address) {
        this.address = address;
        return this;
    }

    public AddEmployeeBuilderNew isAvailable(Boolean isAvailable) {
        this.isAvailable = true;
        return this;
    }

    public User build() {
        return new User(this);
    }

}
