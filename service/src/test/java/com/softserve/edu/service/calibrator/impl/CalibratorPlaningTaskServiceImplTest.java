package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.sun.star.ucb.VerificationMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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

    //@Mock
    //private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @InjectMocks
    CalibratorPlaningTaskServiceImpl calibratorPlaningTaskService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAddNewTask() throws Exception {
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
}