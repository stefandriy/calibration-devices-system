package com.softserve.edu.service.provider;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilder;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilderMainPanel;
import com.softserve.edu.service.provider.buildGraphic.MonthOfYear;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.provider.impl.ProviderEmployeeServiceImpl;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.ArgumentCaptor;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProviderEmployeeServiceImpl.class, Logger.class, GraphicBuilder.class, GraphicBuilderMainPanel.class})
public class ProviderEmployeeServiceImplTest {

	@Mock(name = "providerEmployeeRepository")
	private UserRepository mockProviderEmployeeRepository;

	@Mock
	private VerificationRepository verificationRepository;

	@Mock
	private MailService mockMail;
	private OrganizationRepository organizationRepository;

	@Mock(name = "mail")
	private MailServiceImpl mockMail;

	@Mock(name = "em")
	private EntityManager mockEntityManager;

	@Spy
	private User finalProviderEmployee = new User("name", "generate");

	@InjectMocks
	private ProviderEmployeeServiceImpl providerEmployeeService;

	@Test
	public void testAddEmployee() {

		providerEmployeeService.addEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testUpdateEmployeeWhenEquals() {

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);
		String email = "finalProviderEmployee@gmail.com";
		String firstName = "User";

		when(finalProviderEmployee.getEmail()).thenReturn(email);
		when(finalProviderEmployee.getFirstName()).thenReturn(firstName);

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		verify(finalProviderEmployee, atLeast(2)).getEmail();//
		verify(mockMail).sendNewPasswordMail(eq(email), eq(firstName), anyString());
		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testUpdateEmployeeWhenNotEquals() {
		final String name = "name";
		final String password = "generated";
		final User finalProviderEmployee =
				spy(new User(name, password));

		verify(mockMail, never()).sendNewPasswordMail(anyString(), anyString(), anyString());

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		Assert.assertNull(mockProviderEmployeeRepository
				.save(finalProviderEmployee));
	}

	@Test
	public void testOneProviderEmployeeParametersPropagation() {
		final String username = "username";
		final User mockUser = mock(User.class);

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(mockUser);
		providerEmployeeService.oneProviderEmployee(username);

		Assert.assertEquals(mockUser,
                providerEmployeeService.oneProviderEmployee(username));
	}
/*
	@Test
	public void testGetAllProvidersWhenContains() {

		List<EmployeeDTO> spyProviderListEmployee =
				spy(new ArrayList<>());

		final User spyEmployee = spy(new User("1", "1",
				new Organization("1", "1", "1")));

		List<User> spyUserList = spy(new ArrayList<>());
		spyUserList.add(spyEmployee);

		final EmployeeDTO finalEmpDTO = new EmployeeDTO("1", "1", "1", "1", "1");

		final List<String> spyRoleList = spy(new ArrayList<>());
		spyRoleList.add(UserRole.PROVIDER_ADMIN.name());

		when(
				mockProviderEmployeeRepository.findAllAvailableUsersByRoleAndOrganizationId(
						UserRole.CALIBRATOR_ADMIN, anyLong()).stream().collect(Collectors.toList())).thenReturn(
				spyUserList);

		spyProviderListEmployee.add(finalEmpDTO);

		verify(spyProviderListEmployee).add(finalEmpDTO);

		Assert.assertNotNull(providerEmployeeService.getAllProviders(
                spyRoleList, spyEmployee));
	}

	@Test
	public void testGetAllProvidersWhenNotContains() {

		final User spyEmployee = spy(new User("1", "1"));

		final List<String> spyRoleList = spy(new ArrayList<>());
		spyRoleList.add(UserRole.CALIBRATOR_ADMIN.name());

		EmployeeDTO userPage = mock(EmployeeDTO.class);
		List<EmployeeDTO> providerListEmployee = mock(List.class);

		Assert.assertNotNull(providerEmployeeService.getAllProviders(
                spyRoleList, spyEmployee));
	}
*/
	@Test
	public void testFindByUsername() {

		String username = finalProviderEmployee.getUsername();

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(finalProviderEmployee);

		User actual = providerEmployeeService.findByUsername(username);

		ArgumentCaptor<String> usernameArg = ArgumentCaptor
				.forClass(String.class);

		verify(mockProviderEmployeeRepository).findOne(
				usernameArg.capture());

		Assert.assertEquals(username, usernameArg.getValue());
		Assert.assertEquals(finalProviderEmployee, actual);
	}

	@Test
	public void testGetRoleByUserNam() {

		List<String> mockList = Arrays.asList(UserRole.CALIBRATOR_ADMIN.toString()/*, UserRole.SYS_ADMIN.toString()*/);
		Set<UserRole> mockSet = new HashSet<UserRole>(Arrays.asList(UserRole.CALIBRATOR_ADMIN/*, UserRole.SYS_ADMIN*/));
	//	Collections.reverse(mockList);

		when(mockProviderEmployeeRepository.getRolesByUserName(anyString())).
				thenReturn(mockSet);

		List<String> actual = providerEmployeeService.getRoleByUserNam(anyString());

		verify(mockProviderEmployeeRepository).getRolesByUserName(anyString());

		Assert.assertEquals(mockList, actual);
	}

	@Test
	public void testBuildGraphic() {
		Date fromDate = new Date();
		Date toDate = new Date();
		Long idOrganization = 1L;
		List<User> listOfEmployee = Collections.singletonList(finalProviderEmployee);
		Organization organization = mock(Organization.class);
		Verification verification = mock(Verification.class);
		List<Verification> verifications = Collections.singletonList(verification);
		ProviderEmployeeGraphic providerEmployeeGraphic = mock(ProviderEmployeeGraphic.class);
		List<ProviderEmployeeGraphic> expectedGraphicData = Collections.singletonList(providerEmployeeGraphic);

		PowerMockito.mockStatic(GraphicBuilder.class);
		List<MonthOfYear> monthList = Collections.singletonList(PowerMockito.mock(MonthOfYear.class));


		when(organizationRepository.findOne(idOrganization)).thenReturn(organization);
		when(verificationRepository.findByProviderEmployeeIsNotNullAndProviderAndSentToCalibratorDateBetween(organization, fromDate, toDate))
				.thenReturn(verifications);
		try {
			PowerMockito.when(GraphicBuilder.listOfMonths(fromDate, toDate)).thenReturn(monthList);
			PowerMockito.when(GraphicBuilder.builderData(verifications, monthList, listOfEmployee)).thenReturn(expectedGraphicData);
			List<ProviderEmployeeGraphic> actualGraphicData = providerEmployeeService.buildGraphic(fromDate, toDate, idOrganization, listOfEmployee);
			Assert.assertEquals(expectedGraphicData, actualGraphicData);
		} catch(ParseException e) {
			Assert.fail("Test failed");
		}

	}

	@Test
	public void testBuidGraphicMainPanel() {
		Date fromDate = new Date();
		Date toDate = new Date();
		Long idOrganization = 1L;
		Organization organization = mock(Organization.class);
		Verification verification = mock(Verification.class);
		List<Verification> verifications = Collections.singletonList(verification);
		ProviderEmployeeGraphic providerEmployeeGraphic = mock(ProviderEmployeeGraphic.class);
		List<ProviderEmployeeGraphic> expectedGraphicData = Collections.singletonList(providerEmployeeGraphic);

		PowerMockito.mockStatic(GraphicBuilder.class);
		PowerMockito.mockStatic(GraphicBuilderMainPanel.class);
		List<MonthOfYear> monthList = Collections.singletonList(PowerMockito.mock(MonthOfYear.class));

		when(organizationRepository.findOne(idOrganization)).thenReturn(organization);
		when(verificationRepository.findByProviderAndInitialDateBetween(organization, fromDate, toDate))
				.thenReturn(verifications);

		try {
			PowerMockito.when(GraphicBuilder.listOfMonths(fromDate, toDate)).thenReturn(monthList);
			PowerMockito.when(GraphicBuilderMainPanel.builderData(verifications, monthList, organization)).thenReturn(expectedGraphicData);
			List<ProviderEmployeeGraphic> actualGraphicData = providerEmployeeService.buidGraphicMainPanel(fromDate, toDate, idOrganization);
			Assert.assertEquals(expectedGraphicData, actualGraphicData);
		} catch(ParseException e) {
			Assert.fail("Test failed");
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertToDateIsBlank() {
		String dateString = " ";
		providerEmployeeService.convertToDate(dateString);
	}

	@Test
	public void testConvertToDate() {
		String dateString = "19-10-2015";
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date actual = DATE_FORMAT.parse(dateString);
			Date expected = providerEmployeeService.convertToDate(dateString);
			Assert.assertEquals(expected ,actual);
		} catch(ParseException e) {
			e.getMessage();
			Assert.fail("Wrong actual date formal");
		}
	}


	@Test
	public void testConvertToDateIllegalDateFormat() {
		String date = "19/10";

		PowerMockito.mockStatic(Logger.class);
		Logger logger = PowerMockito.mock(Logger.class);
		PowerMockito.when(Logger.getLogger(any(Class.class))).thenReturn(logger);

		providerEmployeeService.convertToDate(date);

		//verify(logger).error(anyString());

	}

}
