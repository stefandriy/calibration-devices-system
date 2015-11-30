package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.repository.*;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * Created by Yurij Dvornyk on 20.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(CalibratorPlaningTaskServiceImpl.class)
public class CalibratorPlaningTaskServiceImplTest {
    @Mock
    private CalibrationPlanningTaskRepository taskRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private CalibrationModuleRepository moduleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CalibrationDisassemblyTeamRepository teamRepository;

    @Mock
    private CounterTypeRepository counterTypeRepository;

    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @Test
    public void testAddNewTaskForStation() {
        // Date taskDate, String moduleNumber, List<String> verificationsId, String userId
        /*CalibrationModule module = moduleRepository.findByModuleNumber(moduleNumber);
        Set<Verification> verifications = new HashSet<>();
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification wasn't found");
                throw new IllegalArgumentException();
            } else {
                // if (module.getDeviceType() == verification.getDevice().getDeviceType()) {
                verification.setTaskStatus(Status.TEST_PLACE_DETERMINED);
                verificationRepository.save(verification);
                verifications.add(verification);
            }
                *//*else {
                    logger.error("verification and module have different device types");
                    throw new IllegalArgumentException();
                }*//*
        }
        User user = userRepository.findOne(userId);
        CalibrationTask task = new CalibrationTask(module, null, new Date(), taskDate, user, verifications);
        taskRepository.save(task);
        sendTaskToStation(task);*/
    }

    @Test
    public void testAddNewTaskForTeam() {
        // Date taskDate, String serialNumber, List<String> verificationsId, String userId
        /*Set<Verification> verifications = new HashSet<>();
        DisassemblyTeam team = teamRepository.findOne(serialNumber);
        team.setEffectiveTo(taskDate);
        int i = 0;
        boolean counterStatus = false;
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification haven't found");
            } else {
                if (i == 0) {
                    counterStatus = verification.isCounterStatus();
                }
                if (counterStatus == verification.isCounterStatus()) {
                    if (team.getSpecialization() == verification.getDevice().getDeviceType()) {
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
        taskRepository.save(new CalibrationTask(null, team, new Date(), taskDate, user, verifications));*/
    }

    @Test
    public void testFindVerificationsByCalibratorEmployeeAndTaskStatusCount() {
        // returns int ::::: String userName
        /*User user = userRepository.findOne(userName);
        if (user == null) {
            logger.error("Cannot found user!");
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, user.getOrganization().getId()).size();
            }
        }
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK).size();*/

    }

    @Test
    public void testFindByTaskStatusAndCalibratorId() {
        // returns Page<Verification> ::::: Long id, int pageNumber, int itemsPerPage
        // returns Page<Verification> ::::: Long id, int pageNumber, int itemsPerPage
        /*Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, id, pageRequest);*/
    }

    @Test
    public void findVerificationsByCalibratorEmployeeAndTaskStatus() {
        // returns Page<Verification> ::::: String userName, int pageNumber, int itemsPerPage
        /*User user = userRepository.findOne(userName);
        if (user == null) {
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
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK, pageRequest);*/
    }

    @Test
    public void testFindSymbolsAndSizes() {
        // returns List<CounterType> ::::: String verifId
        /*Verification verification = verificationRepository.findOne(verifId);
        if (verification == null) {
            logger.error("Cannot found verification!");
            throw new NullPointerException();
        }
        List<CounterType> counterTypes = counterTypeRepository.findByDeviceId(verification.getDevice().getId());
        if (counterTypes == null) {
            logger.error("Cannot found counter types for verification!");
            throw new NullPointerException();
        }
        return counterTypes;*/
    }
}