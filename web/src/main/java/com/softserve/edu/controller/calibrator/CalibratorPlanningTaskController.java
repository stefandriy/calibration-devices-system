package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.CalibrationTaskDTO;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
//import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibrationModuleService;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "task/", produces = "application/json")
public class CalibratorPlanningTaskController {

    @Autowired
    private CalibratorPlanningTaskService taskService;

    @Autowired
    private CalibrationModuleService moduleService;

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
        Page<Verification> verifications = taskService.findVerificationsByCalibratorEmployeeAndTaskStatus(employeeUser.getUsername(),
                pageNumber, itemsPerPage);
        Long count = Long.valueOf(taskService.findVerificationsByCalibratorEmployeeAndTaskStatusCount(employeeUser.getUsername()));
        List<VerificationPlanningTaskDTO> content = VerificationPageDTOTransformer.toDoFromPageContent(verifications.getContent());
        return new PageDTO<VerificationPlanningTaskDTO>(count, content);
    }

    @RequestMapping(value = "findAllModules/{moduleType}/{workDate}", method = RequestMethod.GET)
    public Map<String,String> findAvailableModules(@PathVariable String moduleType,@PathVariable Date workDate,
                                            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser){
        return moduleService.findAllCalibrationModulsNumbers(moduleType, workDate, employeeUser.getUsername());
    }

}
