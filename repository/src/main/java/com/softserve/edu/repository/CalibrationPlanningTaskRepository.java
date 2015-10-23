package com.softserve.edu.repository;

import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Vasyl on 09.09.2015.
 */
public interface CalibrationPlanningTaskRepository extends CrudRepository<CalibrationTask, Long> {


}
