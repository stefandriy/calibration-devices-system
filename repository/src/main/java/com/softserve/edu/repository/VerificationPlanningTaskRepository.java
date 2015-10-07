package com.softserve.edu.repository;


import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


/**
 * Created by Vasyl on 30.09.2015.
 */
public interface VerificationPlanningTaskRepository extends VerificationRepository {

    Page<Verification> findByCalibratorEmployeeUsernameAndTaskStatusOrderBySentToCalibratorDateDesc(String userName, Status status, Pageable pageable);
    
    List<Verification> findByCalibratorEmployeeUsernameAndTaskStatus(String userName, Status status, Sort sort);
}