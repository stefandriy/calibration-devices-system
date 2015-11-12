package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.calibrator.util.ProtocolDTOTransformer;
import com.softserve.edu.dto.calibrator.ProtocolDTO;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.dto.NewVerificationsFilterSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibratorDigitalProtocolsService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Veronika 5.11.2015
 */
@RestController
@RequestMapping(value="/calibrator/protocols/", produces = "application/json")
public class DigitalVerificationProtocolsCalibratorController {

    @Autowired
    private CalibratorDigitalProtocolsService protocolsService;

    @Autowired
    CalibratorEmployeeService calibratorEmployeeService;

    /**
     * This method calls service whiche returns the list of verifications. The controller transform them with the help of
     * ProtocolDTOTransformer to list of protocolsDTO. It's done to sent to the client only the necessary data.
     * @param pageNumber
     * @param itemsPerPage
     * @param sortCriteria
     * @param sortOrder
     * @param searchData
     * @param employeeUser
     * @return list of ProtocolDTO - data for table with protocols
     */
    @RequestMapping(value="{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
        public PageDTO<ProtocolDTO> getPageOfAllSentVerificationsByStateCalibratorIdAndSearch(
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria,
            @PathVariable String sortOrder, NewVerificationsFilterSearch searchData,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

            User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());

            Status status = Status.TEST_COMPLETED;
            List<Verification> verifications = protocolsService.findPageOfVerificationsByCalibratorIdAndStatus(
                    calibratorEmployee,pageNumber, itemsPerPage, status );
            Long count = protocolsService.countByCalibratorEmployee_usernameAndStatus(calibratorEmployee, status);
            List<ProtocolDTO> content = ProtocolDTOTransformer.toDtofromList(verifications);
//            Long count = Long.valueOf(content.size());

            return new PageDTO<>(count,content);

    }
}
