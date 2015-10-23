package com.softserve.edu.entity.util;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.user.User;


public class AddEmployeeBuilder {
    public String firstName;
    public String lastName;
    public String middleName;
    public String phone;
    public String secondPhone;
    public String email;
    public String username;
    public String password;
    public Address address;
    public Boolean isAvailable;

    public AddEmployeeBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AddEmployeeBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AddEmployeeBuilder middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public AddEmployeeBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public AddEmployeeBuilder secondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
        return this;
    }

    public AddEmployeeBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AddEmployeeBuilder username(String username) {
        this.username = username;
        return this;
    }

    public AddEmployeeBuilder password(String password) {
        this.password = password;
        return this;
    }

    public AddEmployeeBuilder address(Address address) {
        this.address = address;
        return this;
    }

    public AddEmployeeBuilder setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public User build() {
        return new User(this);
    }

}
