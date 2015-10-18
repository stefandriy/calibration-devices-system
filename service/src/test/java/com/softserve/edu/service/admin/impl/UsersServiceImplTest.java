package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

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

//    private User user;
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
//        user = new User("admin","0001");
        expectedGetCountOfVerifications = 0L;
        existsWithUsernameExpected = false;
    }

    @After
    public void tearDown() {
        usersServiceImpl = null;
    }

    @Test
    public void testExistsWithUsername() {
        final String username = "admin";
        stub(userRepository.findOne(username) == null).toReturn(existsWithUsernameExpected);
        boolean actual = userRepository.findOne(username) == null;
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
        final Long organizationId = 1L;
        final int pageNumber = 1;
        final int itemsPerPage = 10;
        String[] expectUserNameList = {
                "111", "222", "333", "444", "555",
                "666", "777", "888", "999", "10"
        };
        List<String> expectList = Arrays.asList(expectUserNameList);
        List<String> actualUserNameList = IteratorUtils.toList(usersServiceImpl.findByOrganizationId(organizationId, pageNumber, itemsPerPage).iterator());
        Assert.assertEquals(expectList, actualUserNameList);
    }*/

    @Test
    public void testGetCountOfVerifications1() {
        final String username = "admin";
        final long organizationId = 1L;
        long actual = usersServiceImpl.getCountOfVerifications(organizationId, username);
        assertEquals(actual, expectedGetCountOfVerifications);
    }
}