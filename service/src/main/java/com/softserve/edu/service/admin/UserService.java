package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;

import java.util.List;

public interface UserService {

    boolean existsWithUsername(String username);

    List<String> getRoles(String username);

    User getUserByRoleAndOrganization(String role, Long organizationId);

    void addEmployee(User user);
}
