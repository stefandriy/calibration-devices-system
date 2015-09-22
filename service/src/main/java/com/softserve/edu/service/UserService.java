package com.softserve.edu.service;

import com.softserve.edu.entity.user.User;

import java.util.List;

public interface UserService {
    public List<User> findByRole(String role);
}