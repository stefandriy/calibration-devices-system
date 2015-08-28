package com.softserve.edu.service.provider;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.user.UserRole;
import com.softserve.edu.entity.util.Roles;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.utils.EmployeeProvider;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeQuary;

public class ProviderEmployeeServiceTest {

	@InjectMocks
	private ProviderEmployeeService providerEmployeeService;

	@Mock
	private UserRepository mockProviderEmployeeRepository;

	@Mock
	private MailService mockMail;

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
		final User finalProviderEmployee = Mockito.spy(new User(name, password));
		//User mockProviderEmployee = Mockito.mock(User.class);

		providerEmployeeService.addEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);
		//mockProviderEmployee.setPassword(passwordEncodedArg.capture());

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Ignore
	@Test
	public void testUpdateEmployee() {
		final String name = "name";
		final String password = "generated";
		final User finalProviderEmployee = new User(name, password);
		User mockProviderEmployee = Mockito.mock(User.class);

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);
		mockProviderEmployee.setPassword(passwordEncodedArg.capture());

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testOneProviderEmployeeParametersPropagation() {
		final String username = "username";
		final User mockUser = Mockito.mock(User.class);

		when(mockProviderEmployeeRepository.getUserByUserName(anyString()))
				.thenReturn(mockUser);
		providerEmployeeService.oneProviderEmployee(username);

		ArgumentCaptor<String> usernameArg = ArgumentCaptor
				.forClass(String.class);
		verify(mockProviderEmployeeRepository).getUserByUserName(
				usernameArg.capture());

		Assert.assertEquals(username, usernameArg.getValue());
		Assert.assertEquals(mockUser,
				providerEmployeeService.oneProviderEmployee(username));

	}

	@Test()
	public void testFindByUserame() { 
		final String username = "userName";
		final User mockUser = Mockito.mock(User.class);

		when(mockProviderEmployeeRepository.findByUsername(anyString()))
				.thenReturn(mockUser);

		Assert.assertEquals(mockUser,
				providerEmployeeService.findByUserame(username));
	}

	@Test
	public void testGetRoleByUserNam() {      
		final String usernam = "usernam";
		final UserRole mockUser = Mockito.mock(UserRole.class);
		final List<UserRole> mockList = Collections.singletonList(mockUser);

		when(mockProviderEmployeeRepository.getRoleByUserNam(anyString()))
				.thenReturn(mockList);

		Assert.assertEquals(mockList,
				providerEmployeeService.getRoleByUserNam(usernam));
	}

}
