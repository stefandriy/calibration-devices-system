package com.softserve.edu.repository;

import com.softserve.edu.entity.verification.calibration.CalibrationTestDataManual;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Misha on 12/13/2015.
 */

@Repository
public interface CalibrationTestDataManualRepository extends CrudRepository<CalibrationTestDataManual, Long> {

    CalibrationTestDataManual findById(Long id);

    @Query("select c from CalibrationTestDataManual c where c.verification.id=:verificationId ")
    CalibrationTestDataManual findByVerificationId(@Param("verificationId") String verificationId);


}
