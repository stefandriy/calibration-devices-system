package documents.ProviderEmployee;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;

import static org.mockito.Mockito.*;


public class ProviderEmployeeTest {

    private VerificationProviderEmployeeService verificationProviderEmployeeService;
    @Mock
    private UserRepository userRepository;
     @Mock
    User user;
    private boolean isOk = false;

    @Mock
    Principal principal;
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public  void oneProviderEmp(){
            User user = new User();
            when(userRepository.findByUsername(null)).thenReturn(user);
        }
}
