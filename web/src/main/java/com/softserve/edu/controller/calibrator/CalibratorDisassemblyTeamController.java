package com.softserve.edu.controller.calibrator;


import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.calibrator.CalibrationDisassemblyTeamDTO;
import com.softserve.edu.service.calibrator.CalibratorDisassemblyTeamService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "disassembly/team", produces = "application/json")
public class CalibratorDisassemblyTeamController {

    @Autowired
    private CalibratorDisassemblyTeamService disassemblyTeamService;


    //TODO
    @RequestMapping(value = "findAll/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    private PageDTO<CalibrationDisassemblyTeamDTO> findAllDisassemblyTeamByByCalibratorId (
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        return new PageDTO<CalibrationDisassemblyTeamDTO>();
    }


    //TODO
    @RequestMapping(value = "findAllDisassemblyTeams/{moduleType}/{workDate}", method = RequestMethod.GET)
    public PageDTO<CalibrationDisassemblyTeamDTO> findAvailableDisassemblyTeams(
            @PathVariable String moduleType, @PathVariable Date workDate,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser){
        //return disassemblyTeamService.findAllCalibratorDisassemblyTeams(moduleType, workDate, employeeUser.getUsername());
        return new PageDTO<CalibrationDisassemblyTeamDTO>();
    }

}
