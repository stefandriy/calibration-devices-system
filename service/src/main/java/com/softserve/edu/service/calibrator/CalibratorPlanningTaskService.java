package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public interface CalibratorPlanningTaskService {

    Page<CalibrationTask> findAllCalibrationTasks(Pageable pageable);

    void addNewTaskForStation(Date taskDate, String serialNumber, List<String> verificationsId, String userId);

    void addNewTaskForTeam (Date taskDate, String serialNumber, List<String> verificationsId, String userId);

    int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName);

    Page<Verification> findByTaskStatusAndCalibratorId(Long Id, int pageNumber,
                                                       int itemsPerPage, String sortCriteria, String sortOrder);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                            int itemsPerPage, String sortCriteria, String sortOrder);
    List<CounterType> findSymbolsAndSizes(String verifId);

}
