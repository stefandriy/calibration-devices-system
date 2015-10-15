package com.softserve.edu.repository.catalogue;

import com.softserve.edu.entity.enumeration.user.EmployeeRole;

/**
 * Created by Bogdan on 14.10.2015.
 */
public interface UserRepositoryCustomisation {

    long countEmployeeVerifications(EmployeeRole employeeRole, String username);
}
