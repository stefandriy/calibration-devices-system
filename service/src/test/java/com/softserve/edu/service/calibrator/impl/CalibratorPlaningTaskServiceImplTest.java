package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationPlanningTask;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.sun.star.ucb.VerificationMode;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.apache.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by Yurko on 20.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(CalibratorPlaningTaskServiceImpl.class)
public class CalibratorPlaningTaskServiceImplTest {

    @Mock
    private CalibrationPlanningTaskRepository taskRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private VerificationPlanningTaskRepository planningTaskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Pageable pageRequest;

    @Mock
    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @InjectMocks
    CalibratorPlaningTaskServiceImpl calibratorPlaningTaskService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAddNewTask() throws Exception {
        boolean actual;
        try {
            CalibratorPlaningTaskServiceImpl cptsi = new CalibratorPlaningTaskServiceImpl();
            cptsi.addNewTask("", "", "", "", null, null, "", "", 1);
            actual = true;
        } catch (Exception ex) {
            actual = false;
        }
        assertEquals(false, actual);
    }

    @Test
    public void test1AddNewTask() throws Exception {
        when(verificationRepository.findOne(anyString())).thenReturn(null);
        doThrow(new RuntimeException()).when(logger).error(anyString());
        boolean actual;
        try {
            calibratorPlaningTaskService.addNewTask("", "", "", "", null, null, "", "", 1);
            actual = true;
        } catch (Exception ex) {
            actual = false;
        }
        assertEquals(false, actual);
    }

    @Test
    public void test2AddNewTask() throws Exception {
        when(verificationRepository.findOne(anyString())).thenReturn(new Verification());
        doThrow(new RuntimeException()).when(logger).error(anyString());
        boolean actual;
        try {
            calibratorPlaningTaskService.addNewTask(null, null, null, null, null, null, null, null, 1);
            actual = true;
        } catch (IllegalArgumentException ex) {
            actual = false;
        }
        assertEquals(false, actual);
    }

    @Test
    public void test3AddNewTask() throws Exception {
        when(verificationRepository.findOne(anyString())).thenReturn(new Verification());
        calibratorPlaningTaskService.addNewTask("vId", "fixed_station", "removed", "cNum", new Date(999L), new Date(999L), "instN123", "", 1);
        verify(taskRepository).save(any(CalibrationPlanningTask.class));
    }

    @Test
    public void testFindVerificationsByCalibratorIdAndReadStatus() throws Exception {
        User user = new User("name", "surname");
        Organization organization = mock(Organization.class);
        when(organization.getId()).thenReturn(123L);

        user.setOrganization(organization);
        when(userRepository.findOne(user.getFirstName())).thenReturn(user);

        Page<Verification> result = new PageImpl<Verification>(new ArrayList<Verification>());
        when(planningTaskRepository.findByCalibratorIdAndReadStatus(anyLong(), any(), any())).thenReturn(result);

        when(calibratorPlaningTaskService.findVerificationsByCalibratorIdAndReadStatus(user.getFirstName(), 1, 1))
                .thenReturn(result);

        calibratorPlaningTaskService.findVerificationsByCalibratorIdAndReadStatus(user.getFirstName(), 1, 1);
        verify(planningTaskRepository).findByCalibratorIdAndReadStatus(anyLong(), any(), any());
    }

    @Test
    public void test1FindVerificationsByCalibratorIdAndReadStatus() throws Exception {
        User user = new User("name", "surname");
        Organization organization = mock(Organization.class);
        when(organization.getId()).thenReturn(123L);

        user.setOrganization(organization);
        when(userRepository.findOne(user.getFirstName())).thenReturn(null);

        doThrow(new RuntimeException()).when(logger).error(anyString());

        boolean actual;
        try {
            calibratorPlaningTaskService.findVerificationsByCalibratorIdAndReadStatus(user.getFirstName(), 1, 1);
            actual = true;
        } catch (RuntimeException ex) {
            actual = false;
        }
        assertEquals(false, actual);
    }
}