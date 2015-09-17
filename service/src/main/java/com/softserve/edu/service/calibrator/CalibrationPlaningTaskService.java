package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.CalibrationPlanningTask;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


/**
 * Created by Vasyl on 09.09.2015.
 */

@Service
public class CalibrationPlaningTaskService {


    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    private Logger logger = Logger.getLogger(CalibrationPlaningTaskService.class);

    @Transactional
    public void addNewTask(String verifId/*, String placeOfcalibration/*, String counterStatus, String counterNumber,
                           Date startDate, Date endDate, String installationNumber, String notes, int floor*/) {
        Verification verification = verificationRepository.findOne(verifId);
        if (verification == null) {
            logger.error("verification haven't found");
        } else {
            CalibrationPlanningTask task = new CalibrationPlanningTask();
            task.setVerification(verification);
            //task.setPlaceOfcalibration(placeOfcalibration);
            /*task.setRemoveStatus(counterStatus);
            task.setSerialNumberOfMeasuringInstallation(installationNumber);
            task.setSerialNumberOfCounter(counterNumber);
            task.setNotes(notes);
            task.setDateOfVisit(startDate);
            task.setDateOfVisitTo(endDate);
            task.setFloor(floor);*/
            taskRepository.save(task);
        }

    }

}
