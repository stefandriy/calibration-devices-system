package com.softserve.edu.service.user;

import com.softserve.edu.entity.user.User;

import java.util.List;

public interface UserService {

    boolean existsWithUsername(String username);

    boolean changeField(String username, String newValue, String typeOfField);

    User getEmployee(String username);

    boolean changePassword(String username, String oldPassword, String newPassword);

    List<User> findByRole(String role);
}