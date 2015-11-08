package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.CalibrationTaskDTO;
import com.softserve.edu.dto.calibrator.SymbolsAndSizesDTO;
import com.softserve.edu.dto.calibrator.TeamDTO;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
//import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.calibrator.CalibrationModuleService;
import com.softserve.edu.service.calibrator.CalibratorDisassemblyTeamService;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/task")
public class CalibratorPlanningTaskController {

    @Autowired
    private CalibratorPlanningTaskService taskService;

    @Autowired
    private CalibrationModuleService moduleService;

    @Autowired
    private CalibratorDisassemblyTeamService teamService;

    private Logger logger = Logger.getLogger(CalibratorPlanningTaskController.class);


    /**
     * This method save task which
     * was formed for the station. If data saved it returns
     * http status OK, else it return http status conflict
     *
     * @param taskDTO
     * @param employeeUser
     * @return ResponseEntity
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    private ResponseEntity saveTaskForStation (@RequestBody CalibrationTaskDTO taskDTO,
                                     @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            taskService.addNewTaskForStation(taskDTO.getTaskDate(), taskDTO.getSerialNumber(), taskDTO.getVerificationsId(), employeeUser.getUsername());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * This method save task which
     * was formed for the team. If data saved it returns
     * http status OK, else it return http status conflict
     *
     * @param taskDTO
     * @param employeeUser
     * @return ResponseEntity
     */
    @RequestMapping(value = "/team/save", method = RequestMethod.POST)
    private ResponseEntity saveTaskForTeam (@RequestBody CalibrationTaskDTO taskDTO,
                                               @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            taskService.addNewTaskForTeam(taskDTO.getTaskDate(), taskDTO.getSerialNumber(), taskDTO.getVerificationsId(), employeeUser.getUsername());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * This method return page of all verifications
     * which assigned for the calibrator employee
     * and has status - planning task
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param employeeUser
     * @return PageDTO<VerificationPlanningTaskDTO>
     */
    @RequestMapping(value = "findAll/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    private PageDTO<VerificationPlanningTaskDTO> findAllVerificationsByCalibratorAndTaskStatus(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                               @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        Page<Verification> verifications = taskService.findVerificationsByCalibratorEmployeeAndTaskStatus(employeeUser.getUsername(),
                pageNumber, itemsPerPage);
        Long count = Long.valueOf(taskService.findVerificationsByCalibratorEmployeeAndTaskStatusCount(employeeUser.getUsername()));
        List<VerificationPlanningTaskDTO> content = VerificationPageDTOTransformer.toDoFromPageContent(verifications.getContent());
        return new PageDTO<VerificationPlanningTaskDTO>(count, content);
    }

    /**
     * This method return list of serial numbers of all available
     * modules filtered by applicationField,
     * workDate and moduleType
     *
     * @param moduleType
     * @param workDate
     * @param applicationFiled
     * @param employeeUser
     * @return List<String>
     */
    @RequestMapping(value = "findAllModules/{moduleType}/{workDate}/{applicationFiled}", method = RequestMethod.GET)
    public List<String> findAvailableModules(@PathVariable String moduleType,@PathVariable Date workDate, @PathVariable String applicationFiled,
                                            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser){
        return moduleService.findAllCalibrationModulsNumbers(moduleType, workDate, applicationFiled, employeeUser.getUsername());
    }

    /**
     * This method return list of serial numbers and
     * team name of all available
     * teams filtered by application field and
     * workDate
     *
     * @param workDate
     * @param applicationFiled
     * @param employeeUser
     * @return List<TeamDTO>
     */
    @RequestMapping(value = "findAllTeams/{workDate}/{applicationFiled}", method = RequestMethod.GET)
    public List<TeamDTO> findAvailableTeams(@PathVariable Date workDate, @PathVariable String applicationFiled,
                                                    @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser){
        List<DisassemblyTeam> teams = teamService.findAllAvaliableTeams(workDate, applicationFiled, employeeUser.getUsername());
        List<TeamDTO> teamDTOs = new ArrayList<>();
        for (DisassemblyTeam team : teams) {
            teamDTOs.add(new TeamDTO(team.getName(), team.getId()));
        }
        return teamDTOs;
    }

    /**
     * This method returns SymbolsAndSizesDTO which contains
     * list of sizes and list of symbols for the
     * counter in verification from the counter type directory
     *
     * @param verificationId
     * @return SymbolsAndSizesDTO
     */
    @RequestMapping(value = "findSymbolsAndSizes/{verificationId}", method = RequestMethod.GET)
    public SymbolsAndSizesDTO findSymbolsAndSizes(@PathVariable String verificationId){
        List<CounterType> counterTypes = new ArrayList<>();
        List<String> sizes = new ArrayList<>();
        List<String> symbols = new ArrayList<>();
        SymbolsAndSizesDTO symbolsAndSizesDTO = new SymbolsAndSizesDTO();
        try {
            counterTypes = taskService.findSymbolsAndSizes(verificationId);
            for (CounterType counterType : counterTypes) {
                sizes.add(counterType.getStandardSize());
                symbols.add(counterType.getSymbol());
            }
            symbolsAndSizesDTO.setSizes(sizes);
            symbolsAndSizesDTO.setSymbols(symbols);
        } catch (Exception e) {
            logger.error("GOT EXCEPTION ", e);
        }
        return symbolsAndSizesDTO;
    }


}
