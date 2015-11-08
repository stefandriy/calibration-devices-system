package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Veronichka on 03.11.2015.
 */
public interface CalibratorDigitalProtocolsService {

    Page<Verification> findByTaskStatusAndCalibratorId(Long Id, int pageNumber,
                                                       int itemsPerPage, Status status);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                          int itemsPerPage, Status status);

    int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName);

    List<Verification> findPageOfVerificationsByCalibratorIdAndStatus(
            User calibratorEmployee, int pageNumber, int itemsPerPage, Status status);

    Long countByCalibratorEmployee_usernameAndStatus (User calibratorEmployee, Status status);
}
