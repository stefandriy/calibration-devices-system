package com.softserve.edu.repository;

import com.softserve.edu.entity.user.ProviderEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderEmployeeRepository extends CrudRepository<ProviderEmployee, String> {}
