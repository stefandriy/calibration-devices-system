package com.softserve.edu.repository;

import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by Vasyl on 30.09.2015.
 */
public interface VerificationPlanningTaskRepository extends VerificationRepository {

    Page<Verification> findByCalibratorIdAndReadStatus(Long calibratorId, ReadStatus status, Pageable pageable, Sort sort);
}
