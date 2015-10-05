package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.CalibrationTaskDTO;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "calibrator/verifications/task/")
public class CalibratorPlanningTaskController {

    @Autowired
    private CalibratorPlanningTaskService taskService;

    private Logger logger = Logger.getLogger(CalibratorPlanningTaskController.class);


    @RequestMapping(value = "add/{verifId}", method = RequestMethod.POST)
    private ResponseEntity saveTask (@PathVariable ("verifId") String verifId, @RequestBody CalibrationTaskDTO taskDTO) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            taskService.addNewTask(verifId, taskDTO.getPlace(), taskDTO.getCounterStatus(), taskDTO.getCounterNumber(),
                                taskDTO.getStartDate(), taskDTO.getEndDate(), taskDTO.getInstallationNumber(), taskDTO.getNotes(), taskDTO.getFloor());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    @RequestMapping(value = "findAll/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    private PageDTO<VerificationPlanningTaskDTO> findAllVerificationsByCalibratorAndReadStatus (@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                          @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        PageDTO<VerificationPlanningTaskDTO> pageDTO = new PageDTO<VerificationPlanningTaskDTO>();
        try{
            Page<Verification> verifications = taskService.findVerificationsByCalibratorEmployeeAndTaskStatus(employeeUser.getUsername(), pageNumber, itemsPerPage);
            List<VerificationPlanningTaskDTO> content = VerificationPageDTOTransformer.toDoFromPageContent(verifications.getContent());
            pageDTO = new PageDTO<VerificationPlanningTaskDTO>(verifications.getTotalElements(), content);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e.getMessage());

        }
        return pageDTO;
    }

}
