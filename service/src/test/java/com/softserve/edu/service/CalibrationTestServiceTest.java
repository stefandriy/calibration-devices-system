package com.softserve.edu.service;

/**
 * Created by Konyk on 13.08.2015.
 */

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;

public class CalibrationTestServiceTest {

    private static final String verificationId = "123";
    private static final Date date = new Date();

    @InjectMocks
    private CalibrationTestService calibrationTestService;

    @Mock
    private CalibrationTestRepository testRepository;

    @Mock
    private CalibrationTest calibrationTest;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    Verification verification;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindTestById() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testFindByVerificationId() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testFindAllCalibrationTests() {
        fail("Not yet implemented"); // TODO
    }


    @Test
    public void testCreateNewTest() {

        when(verificationRepository.findOne(verificationId)).thenReturn(verification);
        doNothing().when(calibrationTest).setVerification(verification);
        doNothing().when(calibrationTest).setDateTest(date);
        when(testRepository.save(calibrationTest)).thenReturn(calibrationTest);

        calibrationTestService.createNewTest(calibrationTest, date, verificationId);
        verify(verificationRepository.findOne(verificationId));

    }

    @Test
    public void testEditTest() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testDeleteTest() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testCreateTestData() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testFindAllTestDataAsociatedWithTest() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testUploadPhotos() {
        fail("Not yet implemented"); // TODO
    }

}