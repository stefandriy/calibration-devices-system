package com.softserve.edu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.role LIKE:role")
    Page<User> findByRoleLikeIgnoreCase(@Param("role") String role, Pageable pageable);

    @Query("SELECT r.role FROM UserRole r INNER JOIN r.users u WHERE u.username=:username")
    String getRoleByUserName(@Param("username") String username);

    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.role =:role AND u.organization.id = :organizationId")
    Page<User> findByRoleAndOrganizationId(@Param("role") String role, @Param("organizationId") Long organizationId,
                                           Pageable pageable);

    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.role =:role AND u.organization.id = :organizationId " +
            " AND u.lastName LIKE :lastName")
    Page<User> findByOrganizationIdAndRoleAndLastNameLikeIgnoreCase(@Param("role") String role,
                                                                    @Param("organizationId") Long organizationId,
                                                                    @Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT r.role FROM UserRole r INNER JOIN r.users u WHERE u.username=:username")
    List<UserRole> getRoleByUserNam(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUserName(@Param("username") String username);

    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.role =  :role AND u.organization.id = :organizationId")
    List<User> getAllProviderUsers(@Param("role") String role, @Param("organizationId") Long organizationId);

    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.role =  :role AND u.organization.id = :organizationId " +
            " AND u.isAvaliable= :isAvaliable")
    List<User> getAllProviderUsersList(@Param("role") String role, @Param("organizationId") Long organizationId,
                                       @Param("isAvaliable") boolean isAvaliable);

    User findByUsername(String userName);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role=:role")
    UserRole getUserRole(@Param("role") String role);

    @Query("SELECT COUNT(u) FROM User u INNER JOIN u.userRoles r WHERE r.role IN :role AND u.organization.id = :organizationId ")
    Long getCountOfEmloyee(@Param("role") List<String> role,
                           @Param("organizationId") Long organizationId);



}
