package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.state.verificator.impl.StateVerificatorEmployeeServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Veronika Herasymenko
 */
@RunWith(MockitoJUnitRunner.class)
public class StateVerificatorEmployeeServiceImplTest {

    @Mock
    private UserRepository stateVerificatorEmployeeRepository;

    @Spy
    private User stateVerificatorEmployee = new User("username", "password");

    @InjectMocks
    private StateVerificatorEmployeeService verificatorEmployeeService = new StateVerificatorEmployeeServiceImpl();

    @Test
    public void testAddEmployee() throws Exception {

        verificatorEmployeeService.addEmployee(stateVerificatorEmployee);

        ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
                .forClass(String.class);

        verify(stateVerificatorEmployee).setPassword(passwordEncodedArg.capture());
        verify(stateVerificatorEmployeeRepository).save(stateVerificatorEmployee);

        Assert.assertEquals(stateVerificatorEmployee.getPassword(),
                passwordEncodedArg.getValue());
    }

    @Test
    public void testOneProviderEmployee() throws Exception {
        final String username = "User";
        when(stateVerificatorEmployeeRepository.findOne(username)).thenReturn(stateVerificatorEmployee);
        User actual = verificatorEmployeeService.oneProviderEmployee(username);
        Assert.assertEquals(stateVerificatorEmployee, actual);
    }
}