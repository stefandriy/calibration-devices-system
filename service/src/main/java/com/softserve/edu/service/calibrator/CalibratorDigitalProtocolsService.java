package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.data.domain.Page;

/**
 * Created by Veronichka on 03.11.2015.
 */
public interface CalibratorDigitalProtocolsService {

    Page<Verification> findByTaskStatusAndCalibratorId(Long Id, int pageNumber,
                                                       int itemsPerPage);

    Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                          int itemsPerPage);

    int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName);
}
