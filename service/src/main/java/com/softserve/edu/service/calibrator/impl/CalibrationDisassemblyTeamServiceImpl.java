package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.service.calibrator.CalibratorDisassemblyTeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
@Transactional(readOnly = true)
public class CalibrationDisassemblyTeamServiceImpl implements CalibratorDisassemblyTeamService {

    //TODO
    @Override
    public Set<DisassemblyTeam> findAll(Long calibratorId) {
        return null;
    }

    @Override
    public List<DisassemblyTeam> findAllCalibratorDisassemblyTeams(String moduleType, Date workDate, String username) {
        return null;
    }
}
