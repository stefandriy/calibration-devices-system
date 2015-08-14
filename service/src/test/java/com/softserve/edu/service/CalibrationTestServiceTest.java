package com.softserve.edu.service;

<<<<<<< Updated upstream
/**
 * Created by Konyk on 13.08.2015.
 */

import static org.junit.Assert.fail;

=======
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
>>>>>>> Stashed changes
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

<<<<<<< Updated upstream
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
=======
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;

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

	@Test(expected = NotAvailableException.class)
	public void testCreateTestData() {
		when(testRepository.findOne(testId)).thenReturn(null);
		calibrationTestService.createTestData(testId, data);
	}

	
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
		// byte[] b = { 1, 3, 4 };
		// doReturn(b).when(IOUtils.toByteArray(file));
		// when(IOUtils.toByteArray(file)).thenReturn(b);
		// when(testIMGRepository.save(any(CalibrationTestIMG.class))).thenReturn(null);

		calibrationTestService.uploadPhotos(null, 1L, "aaa");
		// verify(testIMGRepository.save(any(CalibrationTestIMG.class)));

	}
}
>>>>>>> Stashed changes
