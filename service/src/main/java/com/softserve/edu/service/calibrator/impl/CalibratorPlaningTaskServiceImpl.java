package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.repository.*;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Transactional
public class CalibratorPlaningTaskServiceImpl implements CalibratorPlanningTaskService {

    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private CalibrationModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalibrationDisassemblyTeamRepository teamRepository;

    @Autowired
    private CounterTypeRepository counterTypeRepository;


    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    /**
     * This method save new task for the station. It checks if counter
     * statuses for the verifications are the same, if not
     * @throws IllegalArgumentException(). Also it checks if station
     * device type is is as device type of the verification, if not
     * method @throws IllegalArgumentException().
     *
     * @param taskDate
     * @param serialNumber
     * @param verificationsId
     * @param userId
     */
    @Override
    public void addNewTaskForStation(Date taskDate, String serialNumber, List<String> verificationsId, String userId) {
        CalibrationModule module = moduleRepository.findBySerialNumber(serialNumber);
        Set<Verification> verifications = new HashSet<>();
        int i = 0;
        boolean counterStatus = false;
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification haven't found");
            } else {
                if (i==0){
                    counterStatus = verification.isCounterStatus();
                }
                if (counterStatus == verification.isCounterStatus()) {
                    if (module.getDeviceType() == verification.getDevice().getDeviceType()) {
                        verification.setTaskStatus(Status.TASK_PLANED);
                        verificationRepository.save(verification);
                        verifications.add(verification);
                        i++;
                    } else {
                        logger.error("verification and module has different device types");
                        throw new IllegalArgumentException();
                    }
                } else {
                    logger.error("verifications has different counter status");
                    throw new IllegalArgumentException();
                }
            }
        }
        module.setWorkDate(taskDate);
        moduleRepository.save(module);
        User user = userRepository.findOne(userId);
        taskRepository.save(new CalibrationTask(module, null, new Date(), taskDate, user, verifications));
    }

    /**
     * This method save new task for the team. It checks if counter
     * statuses for the verifications are the same, if not
     * @throws IllegalArgumentException(). Also it checks if station
     * device type is is as device type of the verification, if not
     * method @throws IllegalArgumentException().
     *
     * @param taskDate
     * @param serialNumber
     * @param verificationsId
     * @param userId
     */
    @Override
    public void addNewTaskForTeam(Date taskDate, String serialNumber, List<String> verificationsId, String userId) {
        Set<Verification> verifications = new HashSet<>();
        DisassemblyTeam team = teamRepository.findOne(serialNumber);
        team.setEffectiveTo(taskDate);
        int i = 0;
        boolean counterStatus = false;
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification haven't found");
            } else {
                if (i==0){
                    counterStatus = verification.isCounterStatus();
                }
                if (counterStatus == verification.isCounterStatus()) {
                    if (team.getSpecialization()==verification.getDevice().getDeviceType()){
                        verification.setTaskStatus(Status.TASK_PLANED);
                        verificationRepository.save(verification);
                        verifications.add(verification);
                        i++;
                    } else {
                        logger.error("verification and module has different device types");
                        throw new IllegalArgumentException();
                    }
                } else {
                    logger.error("verifications has different counter status");
                    throw new IllegalArgumentException();
                }
            }
        }
        teamRepository.save(team);
        User user = userRepository.findOne(userId);
        taskRepository.save(new CalibrationTask(null, team, new Date(), taskDate, user, verifications));
    }

    /**
     * This method find count for the verifications with status
     * planning task which assigned for the calibrator employee.
     * If employee has role admin it return count of all verifications with status planning
     * task which related to the calibrator organization
     *
     * @param userName
     * @return count of verifications (int)
     */
    @Override
    public int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, user.getOrganization().getId()).size();
            }
        }
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK).size();

    }

    /**
     * This method returns page of verifications with
     * status planning task filtered by calibrator id,
     * when calibrator is admin
     * and sorted by client address
     *
     * @param id
     * @param pageNumber
     * @param itemsPerPage
     * @return Page<Verification>
     */
    @Override
    public Page<Verification> findByTaskStatusAndCalibratorId(Long id, int pageNumber, int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, id, pageRequest);
    }


    /**
     *  This method returns page of verifications with
     *  status planning task filtered by calibrator id,
     *  and sorted by client address. If user has role
     *  admin it calls method findByTaskStatusAndCalibratorId()
     *
     * @param userName
     * @param pageNumber
     * @param itemsPerPage
     * @return Page<Verification>
     * @throws NullPointerException();
     */
    @Override
    public Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber, int itemsPerPage) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
            throw new NullPointerException();
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return findByTaskStatusAndCalibratorId(user.getOrganization().getId(), pageNumber, itemsPerPage);
            }
        }
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK, pageRequest);
    }

    /**
     * This method returns list of counter types
     * which has the same device id with the verification device id
     *
     * @param verifId
     * @return List<CounterType>
     * @throws NullPointerException();
     */
    @Override
    public List<CounterType> findSymbolsAndSizes(String verifId) {
        Verification verification = verificationRepository.findOne(verifId);
        if (verification == null){
            logger.error("Cannot found verification!");
            throw new NullPointerException();
        }
        List<CounterType> counterTypes = counterTypeRepository.findByDeviceId(verification.getDevice().getId());
        if (counterTypes == null){
            logger.error("Cannot found counter types for verification!");
            throw new NullPointerException();
        }
        return counterTypes;
    }
}