package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.AddEmployeeBuilder;
import com.softserve.edu.entity.util.ConvertSetEnumsToListString;
import com.softserve.edu.repository.UserRepository;

import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.utils.ArchivalEmployeeQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.commons.collections.IteratorUtils;
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
import org.springframework.data.domain.PageRequest;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.UnsupportedEncodingException;
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
@PrepareForTest({ArchivalEmployeeQueryConstructorAdmin.class, IteratorUtils.class, RandomStringUtils.class})
public class UsersServiceImplTest {

    private final Long organizationId = 1L;
    private final String username = "admin";
    private long expectedGetCountOfVerifications;
    private boolean expectedExistsWithUsername;
    private User user;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConvertSetEnumsToListString convertSetEnumsToListString;

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    private Path<Object> objectPath;

    @Mock
    private Predicate predicate;

    @Mock
    private Root<User> root;

    @Mock
    IteratorUtils iteratorUtils;

    @Mock
    MailService mail;

    @Mock
    ArchivalEmployeeQueryConstructorAdmin archivalEmployeeQueryConstructorAdmin;

    @InjectMocks
    private UsersServiceImpl usersServiceImpl;

    @Before
    public void setUp() {
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
    public void testIsExistsWithUsername() {

        stub(userRepository.findOne(username) == null).toReturn(expectedExistsWithUsername);

        boolean actual = usersServiceImpl.isExistsWithUsername(username);
        assertEquals(actual, expectedExistsWithUsername);
    }

    @Test
    public void testGetRoles() throws Exception {
        Set<UserRole> userRole = new HashSet();
        stub(userRepository.getRolesByUserName(username)).toReturn(userRole);
        List<String> strList = ConvertSetEnumsToListString.convertToListString(userRole);
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
        String sortCriteria = "id";
        String sortOrder = "asc";

        CriteriaQuery<User> userCriteriaQuery = mock(CriteriaQuery.class);
        CriteriaQuery<Long> longCriteriaQuery = mock(CriteriaQuery.class);
        TypedQuery<User> userTypedQuery = mock(TypedQuery.class);
        TypedQuery<Long> longTypedQuery = mock(TypedQuery.class);

        PowerMockito.mockStatic(ArchivalEmployeeQueryConstructorAdmin.class);

        PowerMockito.when(ArchivalEmployeeQueryConstructorAdmin.buildSearchQuery(userName, role, firstName, lastName,
                        organization, telephone, sortCriteria, sortOrder, em)).thenReturn(userCriteriaQuery);
        PowerMockito.when(ArchivalEmployeeQueryConstructorAdmin.buildCountQuery(userName, role, firstName,
                        lastName, organization, telephone, em)).thenReturn(longCriteriaQuery);

        stub(em.createQuery(longCriteriaQuery)).toReturn(longTypedQuery);
        stub(em.createQuery(userCriteriaQuery)).toReturn(userTypedQuery);

        List<User> userList = userTypedQuery.getResultList();
        Long count = em.createQuery(ArchivalEmployeeQueryConstructorAdmin.buildCountQuery(userName, role, firstName,
                        lastName, organization, telephone, em)).getSingleResult();

        ListToPageTransformer < User > actual = usersServiceImpl.findPageOfAllEmployees(pageNumber, itemsPerPage,
                userName, role, firstName, lastName, organization, telephone, sortCriteria, sortOrder);

        assertEquals(userList, actual.getContent());
        assertEquals(count, actual.getTotalItems());
    }

    @Test
    public void testAddSysAdmin() throws UnsupportedEncodingException, MessagingException {
        String username = "Admin";
        String password = "pass";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String phone ="+38050000501";
        String email = "mail@mail.com";
        Address address = spy(Address.class);

        usersServiceImpl.addSysAdmin(username, firstName, lastName, middleName, phone, email, address);
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
    public void testEditSysAdmin() throws UnsupportedEncodingException, MessagingException {
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
    public void testEditSysAdmin_with_password_generate() throws UnsupportedEncodingException, MessagingException {
        String username = "Admin";
        String password = "generate";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String phone ="+38050000501";
        String email = "mail@mail.com";
        Address address = mock(Address.class);
        String newPassword = "newpass";

        User sysAdmin = mock(User.class);
        stub(userRepository.findOne(username)).toReturn(sysAdmin);
        when(sysAdmin.getPassword()).thenReturn(password);
        PowerMockito.mockStatic(RandomStringUtils.class);
        PowerMockito.when(RandomStringUtils.randomAlphanumeric(5)).thenReturn(newPassword);
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

    @Test
    public void testFindAllSysAdmins() {
        ListToPageTransformer<User> actual = usersServiceImpl.findAllSysAdmins();
        verify(userRepository, times(2)).findByUserRoleAllIgnoreCase(UserRole.SYS_ADMIN);
    }

    @Test
    public void testFindByOrganizationId()
    {
        final int pageNumber = 1;
        final int itemsPerPage = 10;
        Page<User> userPage = mock(Page.class);
        List<User> expectedFindByOrganizationId = spy(List.class);
        PageRequest pageRequest = new PageRequest(pageNumber, itemsPerPage);
        when(userRepository.findByOrganizationId(organizationId, pageRequest)).thenReturn(userPage);
        PowerMockito.mockStatic(IteratorUtils.class);
        PowerMockito.when(IteratorUtils.toList(userPage.iterator())).thenReturn(expectedFindByOrganizationId);
        List<User> actual = usersServiceImpl.findByOrganizationId(organizationId, pageNumber, itemsPerPage);
        assertEquals(actual, expectedFindByOrganizationId);
    }
}