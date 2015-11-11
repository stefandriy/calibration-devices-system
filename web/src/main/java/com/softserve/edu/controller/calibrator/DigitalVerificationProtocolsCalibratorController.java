package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.calibrator.util.ProtocolDTOTransformer;
import com.softserve.edu.dto.calibrator.ProtocolDTO;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.NewVerificationsFilterSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibratorDigitalProtocolsService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.state.verificator.StateVerificatorEmployeeService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.softserve.edu.dto.provider.VerificationPageDTO;

import java.util.List;

/**
 * Created by Veronichka on 03.11.2015.
 */
@RestController
@RequestMapping(value="/calibrator/protocols/", produces = "application/json")
public class DigitalVerificationProtocolsCalibratorController {

//    @Autowired
//    CalibratorEmployeeService calibratorEmployeeService;

    @Autowired
    private CalibratorDigitalProtocolsService protocolsService;

    @Autowired
    StateVerificatorEmployeeService stateVerificatorEmployeeService;

    @Autowired
    VerificationService verificationService;

    @Autowired
    CalibratorEmployeeService calibratorEmployeeService;

    @RequestMapping(value="{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
        public PageDTO<ProtocolDTO> getPageOfAllSentVerificationsByStateCalibratorIdAndSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
                NewVerificationsFilterSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

            User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
//            ListToPageTransformer<Verification> queryResult = protocolsService.findPageOfVerificationsByCalibratorIdAndCriteriaSearch(
//                    employeeUser.getOrganizationId(), pageNumber, itemsPerPage,
//                    searchData.getDate(),
//                    searchData.getId(),
//                    searchData.getClient_full_name(),
//                    searchData.getStreet(),
//                    searchData.getStatus(),
//                    searchData.getEmployee_last_name(),
//                    sortCriteria,
//                    sortOrder,
//                    calibratorEmployee);


//            Page<Verification> verifications = protocolsService.findVerificationsByCalibratorEmployeeAndTaskStatus(
//                    calibratorEmployee.getUsername() ,pageNumber,itemsPerPage);
//
//            List<ProtocolDTO> content = VerificationPageDTOTransformer.toDoFromPageContent(verifications.getContent());
//            return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);

            Status status = Status.TEST_COMPLETED;
            List<Verification> verifications = protocolsService.findPageOfVerificationsByCalibratorIdAndStatus(calibratorEmployee,pageNumber, itemsPerPage, status );
            //Page<Verification> verifications = protocolsService.findVerificationsByCalibratorEmployeeAndTaskStatus(calibratorEmployee.getUsername(), pageNumber, itemsPerPage, status);
            //Long totalItems = protocolsService.countByCalibratorEmployee_usernameAndStatus(calibratorEmployee, status);
//        verifications.add(new Verification());
            List<ProtocolDTO> content = ProtocolDTOTransformer.toDtofromList(verifications);
            Long count = Long.valueOf(content.size());

            return new PageDTO<>(count,content);

    }
}
