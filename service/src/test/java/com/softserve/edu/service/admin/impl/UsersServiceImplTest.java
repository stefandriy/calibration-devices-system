package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.AddEmployeeBuilder;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.UserRepository;

import com.softserve.edu.service.utils.ArchivalEmployeeQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.SortCriteriaUser;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.lang.String;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by lyubomyr on 18.10.2015.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(UsersServiceImpl.class)
public class UsersServiceImplTest {

    private final Long organizationId = 1L;
    private final String username = "admin";
    private long expectedGetCountOfVerifications;
    private boolean expectedExistsWithUsername;
    private User user;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConvertUserRoleToString convertUserRoleToString;

    @Mock
    private EntityManager em;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<User> criteriaQuery;

    @Mock
    private Path<Object> path;

    @Mock
    private Predicate queryPredicate;

    @Mock
    Root<User> root;

    @InjectMocks
    private UsersServiceImpl usersServiceImpl;

    @Before
    public void initializeMockito() {
        usersServiceImpl = new UsersServiceImpl();
        MockitoAnnotations.initMocks(this);
        expectedGetCountOfVerifications = 0L;
        expectedExistsWithUsername = false;
        user = spy(User.class);

    }

    @After
    public void tearDown() {
        usersServiceImpl = null;
    }

    @Test
    public void testExistsWithUsername() {

        stub(userRepository.findOne(username) == null).toReturn(expectedExistsWithUsername);

        boolean actual = usersServiceImpl.existsWithUsername(username);
        assertEquals(actual, expectedExistsWithUsername);
    }

    @Test
    public void testGetRoles() throws Exception {
        Set<UserRole> userRole = new HashSet();
        stub(userRepository.getRolesByUserName(username)).toReturn(userRole);
        List<String> strList = ConvertUserRoleToString.convertToListString(userRole);
        List<String> actual = usersServiceImpl.getRoles(username);
        Assert.assertEquals(actual,strList);

        verify(userRepository).getRolesByUserName(anyString());
    }

    @Test
    public void testFindPageOfAllEmployees() {
        int pageNumber = 1;
        int itemsPerPage = 10;
        String userName = "username";
        String role = "PROVIDER_ADMIN";
        String firstName = "firstName";
        String lastName = "lastName";
        String organization = null;
        String telephone = "+38050000501";
        String sortCriteria = "SortCriteriaUser.USERNAME";
        String sortOrder = "asc";
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);

        //ListToPageTransformer < User > actual = usersServiceImpl.findPageOfAllEmployees(pageNumber, itemsPerPage, userName,
        //                role, firstName, lastName, organization, telephone, sortCriteria, sortOrder);
    }

    @Test
    public void testAddSysAdmin() {
        String username = "Admin";
        String password = "pass";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String phone ="+38050000501";
        String email = "mail@mail.com";
        Address address = spy(Address.class);

        usersServiceImpl.addSysAdmin(username, password, firstName, lastName, middleName, phone, email, address);
        User spyUser = spy(new AddEmployeeBuilder().username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .phone(phone)
                .email(email)
                .address(address)
                .setIsAvailable(true)
                .build());

        assertTrue(spyUser != null);

        verify(userRepository, times(1)).save(new AddEmployeeBuilder().username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .middleName(middleName)
                .phone(phone)
                .email(email)
                .address(address)
                .setIsAvailable(true)
                .build());
    }

    @Test
    public void testEditSysAdmin() {
        String username = "Admin";
        String password = "pass";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String phone ="+38050000501";
        String email = "mail@mail.com";
        Address address = mock(Address.class);

        User sysAdmin = mock(User.class);
        stub(userRepository.findOne(username)).toReturn(sysAdmin);
        when(sysAdmin.getPassword()).thenReturn(password);
        usersServiceImpl.editSysAdmin(username, password, firstName, lastName, middleName, phone, email, address);
        verify(sysAdmin, times(1)).setAddress(address);
        verify(sysAdmin, times(1)).setEmail(email);
        verify(sysAdmin, times(1)).setFirstName(firstName);
        verify(sysAdmin, times(1)).setLastName(lastName);
        verify(sysAdmin, times(1)).setMiddleName(middleName);
        verify(sysAdmin, times(1)).setPhone(phone);
        verify(sysAdmin, times(1)).setPassword(password);
    }

    @Test
    public void testDeleteSysAdmin() {
        usersServiceImpl.deleteSysAdmin(username);
        verify(userRepository).delete(username);
    }

    @Test
    public void testCountOfVerifications() {
        long actual = usersServiceImpl.countVerifications(user);
        verify(user, times(1)).getUsername();
        //assertEquals(actual, expectedCountOfVerifications);
    }

    @Test
    public void testFindOne() {
        stub(userRepository.findOne(username)).toReturn(user);
        User actual = usersServiceImpl.findOne(username);
        verify(userRepository, times(1)).findOne(username);
        assertEquals(actual, user);
    }
}