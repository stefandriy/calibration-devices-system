package com.softserve.edu.repository;

import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vasyl on 09.09.2015.
 */
@Repository
public interface CalibrationPlanningTaskRepository extends
        PagingAndSortingRepository<CalibrationTask, Long>, JpaSpecificationExecutor {

}
