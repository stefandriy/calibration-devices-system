package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.CalibrationDisassemblyTeamRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.calibrator.CalibratorDisassemblyTeamService;
import com.softserve.edu.service.calibrator.specifications.CalibrationDisassenblyTeamSpecifications;
import com.softserve.edu.service.exceptions.DuplicateRecordException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CalibrationDisassemblyTeamServiceImpl implements CalibratorDisassemblyTeamService {

    @Autowired
    private CalibrationDisassemblyTeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private Specifications<CalibrationDisassenblyTeamSpecifications> specifications;

    private Logger logger = Logger.getLogger(CalibrationDisassemblyTeamServiceImpl.class);

    @Override
    @Transactional
    public List<DisassemblyTeam> getAll() {
        return (List<DisassemblyTeam>) teamRepository.findAll();
    }


    @Override
    @Transactional
    public List<DisassemblyTeam> getByOrganization(Organization organization) {
            return teamRepository.findByOrganization(organization);
    }

    @Override
    @Transactional
    public Page<DisassemblyTeam> getByOrganization(Organization organization, int pageNumber, int itemsPerPage) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return teamRepository.findByOrganization(organization, pageRequest);
    }

    @Override
    @Transactional
    public Page<DisassemblyTeam> findByOrganizationAndSearchAndPagination(int pageNumber, int itemsPerPage,
                                                                          Organization organization, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ? teamRepository.findByOrganization(organization, pageRequest) :
                teamRepository.findByOrganizationAndNameLikeIgnoreCase(organization, "%" + search + "%", pageRequest);
    }

    @Override
    @Transactional
    public void add(DisassemblyTeam disassemblyTeam) throws DuplicateRecordException {
        try {
            if (!teamRepository.exists(disassemblyTeam.getId())) {
                teamRepository.save(disassemblyTeam);
            } else {
                throw new DuplicateRecordException(String.format("Team %s already exists.", disassemblyTeam.getId()));
            }
        } catch (Exception e) {
            throw new DuplicateRecordException(String.format("Team %s already exists.", disassemblyTeam.getId()));
        }
    }

    @Override
    @Transactional
    public DisassemblyTeam findById(String teamId) {
        return teamRepository.findOne(teamId);
    }

    @Override
    @Transactional
    public void edit(String id, String name, Date effectiveTo, Device.DeviceType specialization,
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
    public void delete(String teamId) {
        teamRepository.delete(teamRepository.findOne(teamId));
    }


    /**
     *
     * @param teamUsername
     * @return {@Literal true} if DisassemblyTeam already exist
     * else {@Literal false}
     */
    @Override
    @Transactional
    public boolean isTeamExist(String teamUsername) {
        return teamRepository.exists(teamUsername);
    }


    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("all")
    public List<DisassemblyTeam> findAllAvaliableTeams(Date workDate, String applicationFiled, String userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("Cannot found user!");
        }
        Device.DeviceType deviceType = null;
        if (applicationFiled.equals("WATER")){
            deviceType = Device.DeviceType.WATER;
        } else if (applicationFiled.equals("THERMAL")){
            deviceType = Device.DeviceType.THERMAL;
        }
        List<DisassemblyTeam> teams = new ArrayList<>();
        try{
            teams = teamRepository.findAll(specifications.where(CalibrationDisassenblyTeamSpecifications.
                    disassemblyTeamHasCalibratorId(user.getOrganization().getId())).and(CalibrationDisassenblyTeamSpecifications.
                    disassemblyTeamHasEffectiveTo(workDate)).and(CalibrationDisassenblyTeamSpecifications.
                    disassemblyTeamHasType(deviceType)));
        } catch (Exception e){
            logger.error("Cannot found teams!", e);
        }
        return teams;
    }
}
