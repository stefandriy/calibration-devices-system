package com.softserve.edu.service.calibrator;

import java.util.Date;

public interface CalibrationPlanningTaskService {

     void addNewTask(String verifiedId, String placeOfCalibration, String counterStatus, String counterNumber,
                           Date dateOfVisit, Date dateOfVisitTo, String installationNumber, String notes, int floor);
}
