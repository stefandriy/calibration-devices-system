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
    public void addNewTask(String verifId, String placeOfcalibration, String counterStatus, String counterNumber,
                           Date dateOfVisit, Date dateOfVisitTo, String installationNumber, String notes, int floor) {
        Verification verification = verificationRepository.findOne(verifId);
        if (verification == null) {
            logger.error("verification haven't found");
        } else {
            if ((placeOfcalibration == null) || (counterStatus == null)
                    || (installationNumber == null)) {
                throw new IllegalArgumentException();
            }
            CalibrationPlanningTask task = new CalibrationPlanningTask();
            task.setVerification(verification);
            task.setPlaceOfcalibration(placeOfcalibration);
            task.setRemoveStatus(counterStatus);
            task.setSerialNumberOfMeasuringInstallation(installationNumber);
            task.setSerialNumberOfCounter(counterNumber);
            task.setNotes(notes);
            if (placeOfcalibration == "fixed_station" && counterStatus == "removed") { //TODO: WHY??!!!
                task.setDateOfVisit(null);
                task.setDateOfVisitTo(null);
            } else {
                if ((placeOfcalibration == "fixed_station" && counterStatus == "not_removed") || //TODO: WHY??!!!
                        (placeOfcalibration == "fixed_station")) {
                    if (dateOfVisit == null && dateOfVisitTo == null) {
                        throw new IllegalArgumentException();
                    } else {
                        task.setDateOfVisit(dateOfVisit);
                        task.setDateOfVisitTo(dateOfVisitTo);
                    }
                }
            }
            task.setFloor(floor);
            taskRepository.save(task);
        }
    }
}
