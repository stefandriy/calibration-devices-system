package com.softserve.edu.repository;

import com.softserve.edu.entity.user.User;

import com.softserve.edu.entity.user.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	Page<User> findAll(Pageable pageable);

	@Query("select u from User u inner join u.userRoles r where r.role like:role")
	Page<User> findByRoleLikeIgnoreCase(@Param("role") String role,
			Pageable pageable);

	@Query("select r.role from UserRole r inner join r.users u where u.username=:username")
	String getRoleByUserName(@Param("username") String username);

	@Query("select u from User u inner join u.userRoles r where r.role =:role and u.organization.id = :organizationId")
	Page<User> findByRoleAndOrganizationId(@Param("role") String role,
			@Param("organizationId") Long organizationId, Pageable pageable);

	@Query("select u from User u inner join u.userRoles r where r.role =:role and u.organization.id = :organizationId and u.lastName like :lastName")
	Page<User> findByOrganizationIdAndRoleAndLastNameLikeIgnoreCase(
			@Param("role") String role,
			@Param("organizationId") Long organizationId,
			@Param("lastName") String lastName, Pageable pageable);

	@Query("select u from User u where u.username = :username")
	User getUserByUserName(@Param("username") String username);

	@Query("select u from User u inner join u.userRoles r where r.role =  :role and u.organization.id = :organizationId ")
	List<User> getAllProviderUsers(@Param("role") String role,
			@Param("organizationId") Long organizationId);

	public User findByUsername(String userName);

	@Query("select ur from UserRole ur where ur.role=:role")
	UserRole getUserRole(@Param("role") String role);

}
