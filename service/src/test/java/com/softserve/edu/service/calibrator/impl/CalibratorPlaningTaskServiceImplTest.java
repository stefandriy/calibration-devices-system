package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.apache.log4j.Logger;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Yurij Dvornyk on 20.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(CalibratorPlaningTaskServiceImpl.class)
public class CalibratorPlaningTaskServiceImplTest {

    // region Mocks

    @Mock
    private CalibrationPlanningTaskRepository taskRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private VerificationPlanningTaskRepository planningTaskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CalibrationModuleRepository moduleRepository;

    @Mock
    private Pageable pageRequest;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @InjectMocks
    CalibratorPlaningTaskServiceImpl calibratorPlaningTaskService;

    // endregion

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Tests when verification == null
     * @throws Exception
     */
    @Test
    public void testAddNewTask() throws Exception {
        Date date = new Date(LocalDate.of(2015, 9, 20).toEpochDay());
        List<String> verificationsId = new ArrayList<>();
        verificationsId.add("id1");
        verificationsId.add("id2");
        verificationsId.add("id3");

        when(organizationRepository.findOne(anyLong())).thenReturn(new Organization("", "", ""));
        when(taskRepository.save(any(CalibrationTask.class))).thenReturn(new CalibrationTask());

        CalibrationModule calibrationModule = mock(CalibrationModule.class);
        when(moduleRepository.findCalibrationModuleBySerialNumber(anyString())).thenReturn(calibrationModule);

        calibratorPlaningTaskService.addNewTask(date, "n123", verificationsId, 123L);

        verify(taskRepository).save(any(CalibrationTask.class));
    }

    /**
     * Tests when verification != null
     * @throws Exception
     */
    @Test
    public void test1AddNewTask() throws Exception {
        Date date = new Date(LocalDate.of(2015, 9, 20).toEpochDay());
        List<String> verificationsId = new ArrayList<>();
        verificationsId.add("id1");
        verificationsId.add("id2");
        verificationsId.add("id3");

        when(organizationRepository.findOne(anyLong())).thenReturn(new Organization("", "", ""));
        when(taskRepository.save(any(CalibrationTask.class))).thenReturn(new CalibrationTask());
        when(verificationRepository.findOne(anyString())).thenReturn(new Verification());

        CalibrationModule calibrationModule = mock(CalibrationModule.class);
        when(moduleRepository.findCalibrationModuleBySerialNumber(anyString())).thenReturn(calibrationModule);

        calibratorPlaningTaskService.addNewTask(date, "n123", verificationsId, 123L);

        verify(verificationRepository, times(verificationsId.size())).findOne(anyString());
        verify(moduleRepository).findCalibrationModuleBySerialNumber(anyString());
        verify(moduleRepository).save(any(CalibrationModule.class));
        verify(taskRepository).save(any(CalibrationTask.class));
    }

    /**
     * Tests when data is valid
     * @throws Exception
     */
    @Test
    public void testFindVerificationsByCalibratorEmployeeAndTaskStatusCount() throws Exception {
        String username = "john";

        when(userRepository.findOne(username)).thenReturn(new User(username, "pass"));
        List<Verification> verifications = new ArrayList<>();
        verifications.add(new Verification());
        verifications.add(new Verification());

        when(planningTaskRepository.findByCalibratorEmployeeUsernameAndTaskStatus(anyString(), any(Status.class))).
                thenReturn(verifications);

        int actual = calibratorPlaningTaskService.findVerificationsByCalibratorEmployeeAndTaskStatusCount(username);

        assertEquals(verifications.size(), actual);
    }

    /**
     * Tests when there is logger.error calling: Cannot found user!
     * @throws Exception
     */
    @Test
    public void test1FindVerificationsByCalibratorEmployeeAndTaskStatusCount() throws Exception {
        String username = "john";

        when(userRepository.findOne(username)).thenReturn(null);
        List<Verification> verifications = new ArrayList<>();
        verifications.add(new Verification());
        verifications.add(new Verification());


        doNothing().when(logger).error("Cannot found user!");
        when(planningTaskRepository.findByCalibratorEmployeeUsernameAndTaskStatus(anyString(), any(Status.class))).
                thenReturn(verifications);

        boolean actual;
        try {
            calibratorPlaningTaskService.findVerificationsByCalibratorEmployeeAndTaskStatusCount(username);
            actual = true;
        } catch (Exception ex) {
            actual = false;
        }

        assertEquals(false, actual);
    }

    /**
     * If findByTaskRepository in planningTaskRepository returns null
     * @throws Exception
     */
    @Test
    public void testFindByTaskStatus() throws Exception {
        when(planningTaskRepository.findByTaskStatus(any(Status.class), any(Pageable.class))).
                thenReturn(null);
        Page<Verification> actual = calibratorPlaningTaskService.findByTaskStatus(5, 5);
        verify(planningTaskRepository).findByTaskStatus(any(Status.class), any(Pageable.class));
        assertEquals(null, actual);
    }

    /**
     * When the user can't be found in userRepository
     * @throws Exception
     */
    @Test
    public void testFindVerificationsByCalibratorEmployeeAndTaskStatus() throws Exception {
        String username = "john";
        when(userRepository.findOne(anyString())).thenReturn(null);

        doThrow(Exception.class).when(logger).error(eq("Cannot found user!"));

        boolean actual;
        try {
            Page<Verification> result =
                    calibratorPlaningTaskService.findVerificationsByCalibratorEmployeeAndTaskStatus(username, 5, 5);
            actual = true;
        } catch (Exception ex) {
            actual = false;
        }

        assertEquals(false, actual);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void test1FindVerificationsByCalibratorEmployeeAndTaskStatus() throws Exception {
        String username = "john";
        when(userRepository.findOne(anyString())).thenReturn(new User(username, ""));

        Page<Verification> actual =
                calibratorPlaningTaskService.findVerificationsByCalibratorEmployeeAndTaskStatus(username, 5, 5);

        verify(planningTaskRepository, atLeastOnce()).findByCalibratorEmployeeUsernameAndTaskStatus(
                anyString(), any(Status.class), any(Pageable.class));
    }

    /**
     * Testing with UserRoleSet
     * @throws Exception
     */
    @Test
    public void test2FindVerificationsByCalibratorEmployeeAndTaskStatus() throws Exception {
        String username = "john";
        User user = mock(User.class);
        when(userRepository.findOne(anyString())).thenReturn(user);
        when(user.getUsername()).thenReturn(username);

        Set<UserRole> userRoleSet = new TreeSet<>();
        userRoleSet.add(UserRole.CALIBRATOR_ADMIN);
        when(user.getUserRoles()).thenReturn(userRoleSet);

        Page<Verification> actual =
                calibratorPlaningTaskService.findVerificationsByCalibratorEmployeeAndTaskStatus(username, 5, 5);
        assertTrue(actual == null);
    }
}