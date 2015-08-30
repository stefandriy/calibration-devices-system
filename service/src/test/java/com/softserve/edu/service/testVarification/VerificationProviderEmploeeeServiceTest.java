package com.softserve.edu.service.testVarification;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;

public class VerificationProviderEmploeeeServiceTest {
/*
	@InjectMocks
	private static VerificationProviderEmployeeService employeeService;

	@Mock
	private Logger mockLogger;

	@Mock
	private VerificationRepository mockVerificationRepository;

	@Mock
	private UserRepository mockUserRepository;

	@BeforeClass
	public static void testCreateVerificationProviderEmployeeService() {
		employeeService = new VerificationProviderEmployeeService();
	}

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAssignProviderEmployee() {
		String verificationId = "123";
		User providerEmployee = mock(User.class);
		employeeService
				.assignProviderEmployee(verificationId, providerEmployee);
		verify(mockVerificationRepository, times(1)).findOne(verificationId);
		verify(mockLogger, times(1)).error(anyString());
	}

	@Test
	public void testTwoAssignProviderEmployee() {
		String verificationId = "123";
		User providerEmployee = mock(User.class);
		Verification verification = new Verification();
		Verification spyVerification = spy(verification);
		when(mockVerificationRepository.findOne(verificationId)).thenReturn(
				spyVerification);
		employeeService
				.assignProviderEmployee(verificationId, providerEmployee);
		verify(spyVerification, times(1)).setProviderEmployee(providerEmployee);
		verify(spyVerification, times(1)).setStatus(Status.ACCEPTED);
		verify(spyVerification, times(1)).setReadStatus(ReadStatus.READ);
		verify(mockVerificationRepository, times(1)).save(spyVerification);
	}

	@Test
	public void testThreeAssignProviderEmployee() {
		String verificationId = "123";
		User providerEmployee = null;
		Verification verification = new Verification();
		Verification spyVerification = spy(verification);
		when(mockVerificationRepository.findOne(verificationId)).thenReturn(
				spyVerification);
		employeeService
				.assignProviderEmployee(verificationId, providerEmployee);
		verify(spyVerification, times(1)).setStatus(Status.SENT);
	}


	@Test
	public void testGetProviderEmployeeById() {
		User mockUser = mock(User.class);
		when(this.mockVerificationRepository.getProviderEmployeeById(anyString())).thenReturn(
				mockUser);
		User expected = employeeService.getProviderEmployeeById(anyString());
		Assert.assertEquals(expected, mockUser);
	}

	@Test
	public void testGetVerificationListbyProviderEmployee() {
		List<Verification> expected = new ArrayList<Verification>();
		String userName = "user";
		when(mockVerificationRepository
						.findByProviderEmployeeUsernameAndStatus(userName,
								Status.ACCEPTED)).thenReturn(expected);
		List<Verification> actual = employeeService
				.getVerificationListbyProviderEmployee(userName);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetVerificationListbyCalibratormployee() {
		List<Verification> expected = new ArrayList<Verification>();
		String userName = "user";
		when(
				mockVerificationRepository
						.findByProviderEmployeeUsernameAndStatus(userName,
								Status.IN_PROGRESS)).thenReturn(expected);
		List<Verification> actual = employeeService
				.getVerificationListbyCalibratormployee(userName);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCountByProviderEmployeeTasks() {
		long expected = 0;
		String userName = "user";
		long actual = employeeService.countByProviderEmployeeTasks(userName);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCountByCalibratorEmployeeTasks() {
		long expected = 0;
		String userName = "user";
		long actual = employeeService.countByCalibratorEmployeeTasks(userName);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCountOfEmloyee() {
		long organizationId = 1;
		long expected = 10;
		List<String> roles = new ArrayList<String>();
		when(mockUserRepository.getCountOfEmloyee(roles, organizationId))
				.thenReturn(expected);
		long actual = employeeService.countOfEmloyee(roles, organizationId);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testOneProviderEmployee() {
		String userName = "user";
		User mockUser = mock(User.class);
		when(mockUserRepository.getUserByUserName(userName)).thenReturn(
				mockUser);
		User expected = employeeService.oneProviderEmployee(userName);
		Assert.assertEquals(expected, mockUser);
	}

	@AfterClass
	public static void testCreateVerificationProviderEmployeeServiceNull() {
		employeeService = null;
	}*/
}
