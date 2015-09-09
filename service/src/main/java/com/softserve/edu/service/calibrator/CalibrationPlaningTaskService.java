package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.CalibrationPlanningTask;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Vasyl on 09.09.2015.
 */

@Service
public class CalibrationPlaningTaskService {


    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Transactional
    public void addNewTask(CalibrationPlanningTask task) {
        taskRepository.save(task);
    }

}
