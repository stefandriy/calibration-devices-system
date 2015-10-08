package com.softserve.edu.repository;


import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Created by Vasyl on 30.09.2015.
 */
public interface VerificationPlanningTaskRepository extends VerificationRepository, CrudRepository<Verification, String> {

    List<Verification> findByCalibratorEmployeeUsernameAndTaskStatus(String userName, Status status);

    Page<Verification> findByTaskStatus(Status status, Pageable pageable);

    Page<Verification> findByCalibratorEmployeeUsernameAndTaskStatus(String userName, Status status, Pageable pageable);
}
