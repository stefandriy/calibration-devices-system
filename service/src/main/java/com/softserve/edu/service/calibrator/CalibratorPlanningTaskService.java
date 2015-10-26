package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public interface CalibratorPlanningTaskService {

    void addNewTask(Date taskDate, String serialNumber, List<String> verificationsId, String userId);

    int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName);

    Page<Verification> findByTaskStatus(int pageNumber,
                                        int itemsPerPage);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                                     int itemsPerPage);
//    String createExcelFileFromVerifications(String [] verificationsId) throws IOException;
}
