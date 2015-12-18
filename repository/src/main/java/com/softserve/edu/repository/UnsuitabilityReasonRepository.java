package com.softserve.edu.repository;

import com.softserve.edu.entity.device.UnsuitabilityReason;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UnsuitabilityReasonRepository extends CrudRepository<UnsuitabilityReason, Long> {
    List<UnsuitabilityReason> findAll();
}
