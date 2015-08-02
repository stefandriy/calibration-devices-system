package com.softserve.edu.repository;

import com.softserve.edu.entity.user.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by MAX on 15.07.2015.
 */
public interface UserRoleRepository extends CrudRepository<UserRole, String> {
    UserRole findByRole(String role);

    @Query("SELECT r.role FROM UserRole r INNER JOIN r.users u WHERE u.username=:username")
    List<String> getRoles(@Param("username") String username);

}
