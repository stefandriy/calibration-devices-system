package com.softserve.edu.service.verification;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.VerificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by Volodya NT on 18.08.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class VerificationProviderEmployeeServiceTest {

    @InjectMocks
    VerificationProviderEmployeeService verificationProviderEmployeeService;
    @Mock
    VerificationRepository verificationRepository;
    @Spy
    Verification verification;

    @Test
    public void testAssignProviderEmployee() throws Exception {
        String verificationID = "1";
        User user = mock(User.class);
        Verification verification = new Verification();
        Verification spyVerification = spy(verification);
        when(verificationRepository.findOne(verificationID)).thenReturn(spyVerification);
        verificationProviderEmployeeService.assignProviderEmployee(verificationID,user);
        verify (spyVerification, times(1)).setProviderEmployee(user);
    }
}