package com.softserve.edu.repository;

import java.util.List;

import com.softserve.edu.entity.user.Employee;
import com.softserve.edu.entity.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	Page<User> findAll(Pageable pageable);

	Page<User> findByRoleLikeIgnoreCase(String role, Pageable pageable);

}
