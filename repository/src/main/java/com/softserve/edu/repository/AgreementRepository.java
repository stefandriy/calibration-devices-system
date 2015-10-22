package com.softserve.edu.repository;

import com.softserve.edu.entity.organization.Agreement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AgreementRepository extends CrudRepository<Agreement, Long> {


    Set<Agreement> findAll();
}
