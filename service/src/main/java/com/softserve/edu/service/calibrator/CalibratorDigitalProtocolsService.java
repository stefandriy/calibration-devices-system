package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.springframework.data.domain.Page;

/**
 * Created by Veronichka on 03.11.2015.
 */
public interface CalibratorDigitalProtocolsService {

    ListToPageTransformer<Verification> findPageOfProtocolsByCalibratorId(
            Long organizationId, int pageNumber, int itemsPerPage, String initialDateToSearch, String idToSearch,
            String fullNameToSearch, String streetToSearch, String region, String district, String locality,
            String status, String employeeName, User providerEmployee);
}
