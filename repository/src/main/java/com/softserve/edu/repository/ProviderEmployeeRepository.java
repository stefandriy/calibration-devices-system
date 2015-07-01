package com.softserve.edu.repository;

import com.softserve.edu.entity.user.Employee;
import com.softserve.edu.entity.user.ProviderEmployee;
import com.softserve.edu.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderEmployeeRepository extends CrudRepository<ProviderEmployee, String> {

    Page<User> findAll(Pageable pageable);

    Page<User> findByRoleLikeIgnoreCase(String role, Pageable pageable);

    Page<User> findByRoleAndOrganizationId(String role,Long id, Pageable pageable);

    Page<User> findByOrganizationIdAndRoleAndLastNameLikeIgnoreCase(Long id, String role,String search, Pageable pageable);


    @Query("select u from User u where u.username = :username")
    ProviderEmployee getUserByUserName(@Param("username") String username);

   @Query("select u from User u where u.role =  :role and u.organization.id = :organizationId ")
    List<ProviderEmployee> getAllProviderUsers(@Param("role") String role,
                                               @Param("organizationId")Long organizationId);
}