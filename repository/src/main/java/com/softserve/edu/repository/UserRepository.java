package com.softserve.edu.repository;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM USER u"+
            " INNER JOIN USER_ROLE ur ON u.username = ur.username "+
            " WHERE ur.value LIKE :role", nativeQuery = true)
    List<User> findByRoleLikeIgnoreCase(@Param("role") String role);

    @Query(value = "SELECT elements(u.userRoles) FROM User u WHERE u.username = :username")
    Set<UserRole> getRolesByUserName(@Param("username") String username);

    @Query(value = "SELECT * FROM USER u INNER JOIN USER_ROLE ur WHERE ur.value = :role AND u.organizationId = :organizationId", nativeQuery = true)
    List<User> getAllUsersByRoleAndOrganizationId(@Param("role") String role, @Param("organizationId") Long organizationId);

    @Query(value = "SELECT * FROM USER u INNER JOIN USER_ROLE ur WHERE ur.value = :role AND u.organizationId = :organizationId" +
            " AND u.isAvailable = 1", nativeQuery = true)
    List<User> getAllAvailableUsersByRoleAndOrganizationId(@Param("role") String role, @Param("organizationId") Long organizationId);

    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

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