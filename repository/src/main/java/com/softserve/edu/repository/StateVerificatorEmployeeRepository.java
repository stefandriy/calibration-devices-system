package com.softserve.edu.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.softserve.edu.entity.user.StateVerificatorEmployee;

@Repository
public interface StateVerificatorEmployeeRepository extends CrudRepository<StateVerificatorEmployee, String>{}
