package com.softserve.edu.repository;

import com.softserve.edu.entity.user.CalibratorEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalibratorEmployeeRepository extends CrudRepository<CalibratorEmployee, String>{
}
