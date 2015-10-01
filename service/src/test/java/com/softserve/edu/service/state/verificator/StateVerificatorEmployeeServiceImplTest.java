package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vova on 26.08.15.
 */
public class StateVerificatorEmployeeServiceImplTest {
    final static String username = "username";
    final static String password = "password";

    @InjectMocks
    private StateVerificatorEmployeeService verificatorEmployeeService;

    @Mock
    private UserRepository stateVerificatorEmployeeRepository;

    @Mock
    private User user;

    @Mock
    private User stateVerificatorEmployee;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEmployee() throws Exception {
        when(stateVerificatorEmployee.getPassword()).thenReturn(password);
        String passwordEncoded = new BCryptPasswordEncoder().encode(stateVerificatorEmployee.getPassword());
        verificatorEmployeeService.addEmployee(stateVerificatorEmployee);
     //   verify(stateVerificatorEmployee).setPassword(passwordEncoded));
        verify(stateVerificatorEmployeeRepository).save(stateVerificatorEmployee);
    }

    @Test
    public void testOneProviderEmployee() throws Exception {
        when(stateVerificatorEmployeeRepository.findOne(username)).thenReturn(user);
        Assert.assertEquals(user, verificatorEmployeeService.oneProviderEmployee(username));
    }
}