package com.softserve.edu.repository;

import java.util.List;
import com.softserve.edu.entity.util.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softserve.edu.entity.user.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

//	Page<User> findAll(Pageable pageable);
//
//	Page<User> findByRoleLikeIgnoreCase(String role, Pageable pageable);
//
//    Page<User> findByRoleAndOrganizationId(String role,Long id, Pageable pageable);
//
//    Page<User> findByOrganizationIdAndRoleAndLastNameLikeIgnoreCase(Long id, String role,String search, Pageable pageable);
//
//
    @Query("select u from User u where u.username = :username")
    Employee getUserByUserName(@Param("username") String username);

//   @Query("select u from User u where u.role =  :role and u.organization.id = :organizationId ")
//    List<Employee> getAllProviderUsers(@Param("role") String role,
//                                               @Param("organizationId")Long organizationId);
//   
//   public Employee findByUsername(String userName);

}
