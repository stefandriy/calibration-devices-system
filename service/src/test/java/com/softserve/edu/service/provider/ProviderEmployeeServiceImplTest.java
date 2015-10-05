package com.softserve.edu.service.provider;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.utils.EmployeeDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
public class ProviderEmployeeServiceImplTest {

	@InjectMocks
	private ProviderEmployeeService providerEmployeeService;

	@Mock
	private UserRepository mockProviderEmployeeRepository;

	@Mock
	private MailServiceImpl mockMail;

	@Mock
	private EntityManager mockEntityManager;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddEmployee() {
		final String name = "name";
		final String password = "pass";
		final User finalProviderEmployee = Mockito
				.spy(new User(name, password));

		providerEmployeeService.addEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Ignore
	@Test
	public void testUpdateEmployeeWhenEquals() {
		final String name = "name";
		final String password = "generate";
		final User finalProviderEmployee = Mockito
				.spy(new User(name, password));

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testUpdateEmployeeWhenNotEquals() {
		final String name = "name";
		final String password = "generated";
		final User finalProviderEmployee = Mockito
				.spy(new User(name, password));

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		Assert.assertNull(mockProviderEmployeeRepository
				.save(finalProviderEmployee));
	}

	@Test
	public void testOneProviderEmployeeParametersPropagation() {
		final String username = "username";
		final User mockUser = Mockito.mock(User.class);

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(mockUser);
		providerEmployeeService.oneProviderEmployee(username);

		ArgumentCaptor<String> usernameArg = ArgumentCaptor
				.forClass(String.class);
		verify(mockProviderEmployeeRepository).findOne(
				usernameArg.capture());

		Assert.assertEquals(username, usernameArg.getValue());
		Assert.assertEquals(mockUser,
                providerEmployeeService.oneProviderEmployee(username));

	}

	@Test
	public void testGetAllProvidersWhenContains() {

		List<EmployeeDTO> spyProviderListEmployee = Mockito
				.spy(new ArrayList<>());

		final User spyEmployee = Mockito.spy(new User("1", "1",
				new Organization("1", "1", "1")));

		List<User> spyUserList = Mockito.spy(new ArrayList<>());
		spyUserList.add(spyEmployee);

		final EmployeeDTO finalEmpDTO = new EmployeeDTO("1", "1", "1", "1", "1");

		final List<String> spyRoleList = Mockito.spy(new ArrayList<>());
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

		final User spyEmployee = Mockito.spy(new User("1", "1"));

		final List<String> spyRoleList = Mockito.spy(new ArrayList<>());
		spyRoleList.add(UserRole.CALIBRATOR_ADMIN.name());

		EmployeeDTO userPage = Mockito.mock(EmployeeDTO.class);
		List<EmployeeDTO> providerListEmployee = Mockito.mock(List.class);

		Assert.assertNotNull(providerEmployeeService.getAllProviders(
                spyRoleList, spyEmployee));
	}

	@Test
	public void testFindByUserame() {
		final String username = "userName";
		final User mockUser = Mockito.mock(User.class);

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(mockUser);

		Assert.assertEquals(mockUser,
                providerEmployeeService.findByUserame(username));
	}

	@Test
	public void testGetRoleByUserNam() {
		final String usernam = "usernam";
		final String mockUser = anyString();
		final List<String> mockList = Collections.singletonList(mockUser);

		when(mockProviderEmployeeRepository.getRolesByUserName(anyString()))
				.thenReturn(ConvertUserRoleToString.convertToSetUserRole(mockList));

		Assert.assertEquals(mockList,
                providerEmployeeService.getRoleByUserNam(usernam));
	}

}
