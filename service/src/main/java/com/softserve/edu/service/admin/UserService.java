package com.softserve.edu.service.admin;

import com.softserve.edu.entity.user.User;

import java.util.List;

public interface UserService {

    boolean existsWithUsername(String username);

    List<String> getRoles(String username);

    void addEmployee(User user);

    List<User> findByOrganizationId(Long organizationId, int pageNumber, int itemsPerPage);

    Long getCountOfVerifications(Long organizationId, String username);

    User findOne(String username);
}
