package com.softserve.edu.repository.catalogue;

import com.softserve.edu.entity.enumeration.user.UserRole;

public interface UserRepositoryCustom {

    long countEmployeeVerifications(UserRole employeeRole, String username);
}
