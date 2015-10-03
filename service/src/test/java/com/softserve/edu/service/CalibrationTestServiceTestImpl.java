package com.softserve.edu.service;

import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CalibrationTestServiceTestImpl {
/*
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
	private List<CalibrationTestData> listCalibrationTestData;

	@Mock
	private ArrayList<CalibrationTest> list;

	@Mock
	private CalibrationTestIMGRepository testIMGRepository;

	@Mock
	InputStream file;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindTestById() throws Exception {
		when(testRepository.findOne(testId)).thenReturn(calibrationTest);
		Assert.assertEquals(calibrationTest, calibrationTestService.findTestById(testId));
	}

	@Test
	public void testFindByVerificationId() throws Exception {
		when(testRepository.findByVerificationId(verificationId)).thenReturn(calibrationTest);
		Assert.assertEquals(calibrationTestService.findByVerificationId(verificationId), calibrationTest);
	}

	@Test
	public void testFindAllCalibrationTests() throws Exception {
		when(testRepository.findAll()).thenReturn(list);
		CalibrationTestList foundTests = new CalibrationTestList(list);
		Assert.assertEquals(foundTests.getCalibrationTests(), calibrationTestService.findAllCalibrationTests().getCalibrationTests());
	}

	@Ignore
	@Test
	public void testGetCalibrationTestsBySearchAndPagination() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCreateNewTest() {
		when(verificationRepository.findOne(verificationId)).thenReturn(verification);
		calibrationTestService.createNewTest(calibrationTest, date, verificationId);
		verify(verificationRepository).findOne(verificationId);
		verify(calibrationTest).setVerification(verification);
		verify(calibrationTest).setDateTest(date);
		verify(testRepository).save(calibrationTest);
	}

	@Test
	public void testDeleteTest() {
		when(testRepository.findOne(testId)).thenReturn(calibrationTest);
		Assert.assertEquals(calibrationTest, calibrationTestService.deleteTest(testId));
		verify(testRepository).delete(testId);
	}

	@Test
	public void testCreateTestData() {
		ArgumentCaptor<Long> id = ArgumentCaptor.forClass(Long.class);
		calibrationTestService.createTestData(testId, data);
		verify(testRepository).findOne(id.capture());
		assertEquals(testId, id.getValue());
	}

	@Test
	public void testEditTest() {
		final String name = "name";
		ArgumentCaptor<String> nameArg = ArgumentCaptor.forClass(String.class);
		when(testRepository.findOne(testId)).thenReturn(calibrationTest);
		CalibrationTest calibrationTest = calibrationTestService.editTest(testId, name, 1, 1, 1d, 1d, "status",
				CalibrationTestResult.SUCCESS);
		verify(calibrationTest).setName(nameArg.capture());
		assertEquals(name, nameArg.getValue());
	}

	
	@Test(expected = NotAvailableException.class)
	public void testFindAllTestDataAsociatedWithTest() throws Exception {
		when(testRepository.findOne(testId)).thenReturn(null);
		calibrationTestService.findAllTestDataAsociatedWithTest(testId);
	}

	@Test
	public void testSecondFindAllTestDataAsociatedWithTest() throws Exception {
		when(testRepository.findOne(testId)).thenReturn(calibrationTest);
		when(dataRepository.findByCalibrationTestId(testId)).thenReturn(listCalibrationTestData);
		CalibrationTestDataList calibrationTestDataList = calibrationTestService.findAllTestDataAsociatedWithTest(testId);
		Assert.assertEquals(testId, calibrationTestDataList.getTestId());
		Assert.assertEquals(listCalibrationTestData, calibrationTestDataList.getListTestData());
	}*/
}
