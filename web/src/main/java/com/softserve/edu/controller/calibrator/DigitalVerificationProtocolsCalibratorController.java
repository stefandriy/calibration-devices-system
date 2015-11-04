package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibratorDigitalProtocolsService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
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

//    @RequestMapping(value="{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
//    public PageDTO<VerificationPageDTO> getPageOfAllProtocolsByCalibratorId(
//            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
//            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
//        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
//
//
//
//    }

    @RequestMapping(value="{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    private PageDTO<VerificationPlanningTaskDTO> findAllVerificationsByCalibratorAndReadStatus (@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                                @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        Page<Verification> verifications = protocolsService.findVerificationsByCalibratorEmployeeAndTaskStatus(employeeUser.getUsername(),
                pageNumber, itemsPerPage);
        Long count = Long.valueOf(protocolsService.findVerificationsByCalibratorEmployeeAndTaskStatusCount(employeeUser.getUsername()));
        List<VerificationPlanningTaskDTO> content = VerificationPageDTOTransformer.toDoFromPageContent(verifications.getContent());
        return new PageDTO<VerificationPlanningTaskDTO>(count, content);
    }
}
