package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.UserRepository;
import org.apache.commons.collections.IteratorUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.String;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by lyubomyr on 18.10.2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceImplTest {

    private final List<User> userList = new ArrayList<>();
    private final List<String> strList = new ArrayList<>();
    private final Long organizationId = 1L;
    private final int pageNumber = 1;
    private final int itemsPerPage = 10;
    private final String username = "admin";
    private long expectedGetCountOfVerifications;
    private boolean existsWithUsernameExpected;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsersServiceImpl usersServiceImpl;

    @Before
    public void initializeMockito() {
        usersServiceImpl = new UsersServiceImpl();
        MockitoAnnotations.initMocks(this);
        expectedGetCountOfVerifications = 0L;
        existsWithUsernameExpected = false;
        userList.add(new User("admin","1234"));
        strList.add("admin");

    }

    @After
    public void tearDown() {
        usersServiceImpl = null;
    }

    @Test
    public void testExistsWithUsername() {

        stub(userRepository.findOne(username) == null).toReturn(existsWithUsernameExpected);

        boolean actual = (userRepository.findOne(username) == null);
        assertEquals(actual, existsWithUsernameExpected);
    }

    @Test
    public void testGetRoles() {
/*        final String usernam = "admin";
        final String mockUser = anyString();
        final List<String> mockList = Collections.singletonList(mockUser);

        when(userRepository.getRolesByUserName(anyString()))
                .thenReturn(ConvertUserRoleToString.convertToSetUserRole(mockList));

        Assert.assertEquals(mockList,
                usersServiceImpl.getRoles(usernam));*/

        /*stub(ConvertUserRoleToString.convertToListString(userRepository.getRolesByUserName(username))).toReturn(strList);
        List<String> actual = usersServiceImpl.getRoles(username);
        assertEquals(strList, actual);*/

        usersServiceImpl.getRoles(anyString());
        verify(userRepository).getRolesByUserName(anyString());
    }

    @Test
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
    }

/*    @Test
    public void testFindByOrganizationId() {
        stub(IteratorUtils.toList(userRepository.findByOrganizationId(organizationId, new PageRequest(pageNumber, itemsPerPage)).iterator())).toReturn(userList);
        List<User> actual = usersServiceImpl.findByOrganizationId(organizationId, pageNumber, itemsPerPage);

        assertEquals(userList, actual);
    }*/

    @Test
    public void testGetCountOfVerifications1() {
        long actual = usersServiceImpl.getCountOfVerifications(organizationId, username);
        assertEquals(actual, expectedGetCountOfVerifications);
    }
}