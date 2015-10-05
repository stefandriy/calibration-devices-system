package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface CalibratorPlanningTaskService {

     void addNewTask(String verifiedId, String placeOfCalibration, String counterStatus, String counterNumber,
                           Date dateOfVisit, Date dateOfVisitTo, String installationNumber, String notes, int floor);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                          int itemsPerPage);
}
