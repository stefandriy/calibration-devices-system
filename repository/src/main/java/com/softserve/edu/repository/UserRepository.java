package com.softserve.edu.repository;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends org.springframework.data.repository.CrudRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u" +
            " FROM User u" +
            " WHERE  :userRole in elements(u.userRoles)")
    Set<User> findByUserRoleAllIgnoreCase(@Param("userRole") UserRole userRole);

    @Query("SELECT elements(u.userRoles)" +
            " FROM User u" +
            " WHERE u.username = :username")
    Set<UserRole> getRolesByUserName(@Param("username") String username);

    @Query("SELECT u" +
            " FROM User u" +
            " WHERE :userRole in elements(u.userRoles)" +
            " AND u.organization.id = :organizationId")
    Set<User> findByUserRoleAndOrganizationId(@Param("userRole") UserRole userRole,
                                              @Param("organizationId") Long organizationId);

    @Query("SELECT u" +
            " FROM User u" +
            " WHERE :userRole  in elements(u.userRoles)" +
            " AND u.organization.id = :organizationId" +
            " AND u.isAvailable = 1")
    Set<User> findAllAvailableUsersByRoleAndOrganizationId(@Param("userRole") UserRole userRole,
                                                           @Param("organizationId") Long organizationId);

    Page<User> findByOrganizationId(Long organizationId, Pageable pageable);

    /*@Query("SELECT COUNT(v.providerEmployee_username) as verifications_count, u.*"
        +" FROM user u"
        +" LEFT OUTER JOIN verification v ON v.providerEmployee_username = u.username"
        +" WHERE u.organizationId = 43"
        +" GROUP BY u.username"
    )
    Long getCountOfEmployeeVerifications(@Param("organizationId") Long organizationId, @Param("username") String username);*/
}
//    SELECT COUNT(v.providerEmployee_username) as verifications_count, u.*
//        FROM user u
//        LEFT OUTER JOIN verification v
//        ON v.providerEmployee_username = u.username
//        WHERE u.organizationId = 43
//        GROUP BY u.username