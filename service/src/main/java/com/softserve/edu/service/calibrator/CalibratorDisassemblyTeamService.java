package com.softserve.edu.service.calibrator;


import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CalibratorDisassemblyTeamService {
    Set<DisassemblyTeam> findAll(Long calibratorId);

    List<DisassemblyTeam> findAllCalibratorDisassemblyTeams(String moduleType, Date workDate, String username);

}
