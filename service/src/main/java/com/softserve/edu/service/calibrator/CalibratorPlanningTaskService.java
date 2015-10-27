package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.verification.Verification;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public interface CalibratorPlanningTaskService {

    void addNewTaskForStation(Date taskDate, String serialNumber, List<String> verificationsId, String userId);

    int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName);

    Page<Verification> findByTaskStatus(int pageNumber,
                                        int itemsPerPage);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                                     int itemsPerPage);

}
