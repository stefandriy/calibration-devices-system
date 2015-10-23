package com.softserve.edu.service.calibrator;


import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.service.exceptions.DuplicateRecordException;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface CalibratorDisassemblyTeamService {

    List<DisassemblyTeam> getAll();

    Page<DisassemblyTeam> getDisassemblyTeamBySearchAndPagination(int pageNumber,int itemsPerPage, String search);

    void addDisassemblyTeam(DisassemblyTeam disassemblyTeam) throws DuplicateRecordException;

    DisassemblyTeam getDisassemblyTeamById(String teamId);

    void editDisassemblyTeam(String id, String name, Date effectiveTo, Device.DeviceType specialization, String leaderFullName,
                             String leaderPhone, String leaderEmail);

    void deleteDisassemblyTeam(String teamId);

}
