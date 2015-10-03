package com.softserve.edu.controller.calibrator;

import com.softserve.edu.dto.calibrator.CalibrationTaskDTO;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "findAll/{verifId}", method = RequestMethod.GET)
    private ResponseEntity findAllVerificationsByCalibratorAndReadStatus (@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                          @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        HttpStatus httpStatus = HttpStatus.OK;
        taskService.findVerificationsByCalibratorIdAndReadStatus(employeeUser.getUsername(), pageNumber, itemsPerPage);
        return new ResponseEntity(httpStatus);
    }

}
