package com.softserve.edu.repository;

import java.util.List;

import com.softserve.edu.entity.user.Employee;
import com.softserve.edu.entity.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softserve.edu.entity.user.User;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	Page<User> findAll(Pageable pageable);

	Page<User> findByRoleLikeIgnoreCase(String role, Pageable pageable);

	@Query(value = "SELECT USER.username FROM USER WHERE USER.organization_id = ?1", nativeQuery = true)
	String findUsernameByOrganizationId(Long id);
	
}
