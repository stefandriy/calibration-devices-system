package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.repository.CalibrationDisassemblyTeamRepository;
import com.softserve.edu.service.calibrator.CalibratorDisassemblyTeamService;
import com.softserve.edu.service.exceptions.DuplicateRecordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class CalibrationDisassemblyTeamServiceImpl implements CalibratorDisassemblyTeamService {

    @Autowired
    private CalibrationDisassemblyTeamRepository teamRepository;

    @Override
    @Transactional
    public List<DisassemblyTeam> getAll() {
        return (List<DisassemblyTeam>) teamRepository.findAll();
    }

    @Override
    @Transactional
    public Page<DisassemblyTeam> getDisassemblyTeamBySearchAndPagination(int pageNumber, int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ? teamRepository.findAll(pageRequest) :
                teamRepository.findByNameLikeIgnoreCase("%" + search + "%", pageRequest);
    }

    @Override
    @Transactional
    public void addDisassemblyTeam(DisassemblyTeam disassemblyTeam) throws DuplicateRecordException {
        try {
            teamRepository.save(disassemblyTeam);
        } catch (Exception e) {
            throw new DuplicateRecordException(String.format("Team %s already exists.", disassemblyTeam.getId()));
        }
    }

    @Override
    @Transactional
    public DisassemblyTeam getDisassemblyTeamById(String teamId) {
        return teamRepository.findOne(teamId);
    }

    @Override
    @Transactional
    public void editDisassemblyTeam(String id, String name, Date effectiveTo, DeviceType specialization,
                                    String leaderFullName, String leaderPhone, String leaderEmail) {
        DisassemblyTeam team = teamRepository.findOne(id);
        team.setName(name);
        team.setEffectiveTo(effectiveTo);
        team.setSpecialization(specialization);
        team.setLeaderFullName(leaderFullName);
        team.setLeaderPhone(leaderPhone);
        team.setLeaderEmail(leaderEmail);
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void deleteDisassemblyTeam(String teamId) {
        teamRepository.delete(teamRepository.findOne(teamId));
    }
}
