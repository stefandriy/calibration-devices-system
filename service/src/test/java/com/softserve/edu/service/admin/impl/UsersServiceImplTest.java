package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.UserRepository;

import com.softserve.edu.service.utils.ListToPageTransformer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.lang.String;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by lyubomyr on 18.10.2015.
 */

@RunWith(MockitoJUnitRunner.class)
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

/*    @Test
    public void testFindPageOfAllEmployees() {
        int pageNumber = 1;
        int itemsPerPage = 10;
        String userName="username";
        String role="Role";
        String firstName="firstName";
        String lastName="lastName";
        String organization="organization";
        String telephone="telephone";
        String sortCriteria="";
        String sortOrder="asc";
        ListToPageTransformer<User> actual = usersServiceImpl.findPageOfAllEmployees(pageNumber, itemsPerPage, userName,
                role, firstName, lastName, organization, telephone, sortCriteria, sortOrder);
    }*/

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

    /*@Test
    public void testAddEmployee() {
        final String name = "admin";
        final String password = "pass";
        final User finalUsersEmployee = Mockito
                .spy(new User(name, password));

        usersServiceImpl.addEmployee(finalUsersEmployee);

        ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
                .forClass(String.class);

        verify(finalUsersEmployee).setPassword(passwordEncodedArg.capture());

        Assert.assertEquals(finalUsersEmployee.getPassword(),
                passwordEncodedArg.getValue());
    }*/
}