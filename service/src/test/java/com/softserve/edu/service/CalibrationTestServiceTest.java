package com.softserve.edu.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;

public class CalibrationTestServiceTest {

	private static final String verificationId = "123";
	private static final Date date = new Date();
	private static final Long testId = 1L;

	@InjectMocks
	private CalibrationTestService calibrationTestService;

	@Mock
	private CalibrationTestRepository testRepository;

	@Mock
	private CalibrationTest calibrationTest;

	@Mock
	private VerificationRepository verificationRepository;

	@Mock
	private Verification verification;

	@Mock
	private CalibrationTestData data;

	@Mock
	private CalibrationTestDataRepository dataRepository;

	@Mock
	private CalibrationTestIMGRepository testIMGRepository;

	@Mock
	InputStream file;

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
	public void testGetCalibrationTestsBySearchAndPagination() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCreateNewTest() {
		when(verificationRepository.findOne(verificationId)).thenReturn(verification);
		calibrationTestService.createNewTest(calibrationTest, date, verificationId);
		verify(verificationRepository.findOne(verificationId));
	}

	@Test
	public void testEditTest() {
		when(testRepository.findOne(testId)).thenReturn(calibrationTest);
		calibrationTestService.editTest(testId, "a", 1, 1, 1d, 1d, "status", CalibrationTestResult.SUCCESS);
		verify(testRepository.findOne(testId));
	}

	@Test
	public void testDeleteTest() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCreateTestData() {
		when(testRepository.findOne(testId)).thenReturn(null);
		calibrationTestService.createTestData(testId, data);
		verify(dataRepository).save(data);
	}

	/**
	 * 
	 */
	@Test
	public void testFindAllTestDataAsociatedWithTest() {

		Long calibrationTestId = 1L;
		when(testRepository.findOne(calibrationTestId)).thenReturn(calibrationTest);
		when(dataRepository.findByCalibrationTestId(calibrationTestId)).thenReturn(null);

		calibrationTestService.findAllTestDataAsociatedWithTest(calibrationTestId);
		verify(dataRepository).findByCalibrationTestId(calibrationTestId);
	}

	@Test(expected = NullPointerException.class)
	public void testUploadPhotos() throws IOException {
		calibrationTestService.uploadPhotos(null, 1L, "aaa");
	}
}
