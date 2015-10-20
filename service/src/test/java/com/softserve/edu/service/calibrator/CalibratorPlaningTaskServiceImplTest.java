package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationPlanningTask;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.impl.CalibratorPlaningTaskServiceImpl;
import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.star.ucb.VerificationMode;
import com.sun.star.util.DateTime;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Yurko on 18.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalibratorPlaningTaskServiceImplTest {
    private CalibratorPlaningTaskServiceImpl calibratorPlaningTaskService;

    @org.junit.Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        calibratorPlaningTaskService = new CalibratorPlaningTaskServiceImpl();
    }

    @org.junit.After
    public void tearDown() {
        calibratorPlaningTaskService = null;
    }

    /**
     * If we pass empty parameters, we should get exception
     *
     * @throws Exception
     */
    @Test
    public void testAddNewTask() throws Exception {
        boolean result;
        try {
            calibratorPlaningTaskService.addNewTask("", "", "", "", null, null, "", "", 0);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(false, result);
    }

    /**
     * If we pass empty parameters, we should get exception
     *
     * @throws Exception
     */
    @Test
    public void testFindVerificationsByCalibratorIdAndReadStatus() throws Exception {
        boolean result;
        try {
            Page<Verification> p = calibratorPlaningTaskService.findVerificationsByCalibratorIdAndReadStatus("", 0, 5);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(false, result);
    }
}