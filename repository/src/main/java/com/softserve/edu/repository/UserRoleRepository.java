package com.softserve.edu.repository;

import com.softserve.edu.entity.user.UserRole;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by MAX on 15.07.2015.
 */
public interface UserRoleRepository extends CrudRepository<UserRole, String> {
    UserRole findByRole(String role);

}
