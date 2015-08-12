package documents.ProviderEmployee;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


public class ProviderEmployeeTest {

    private VerificationProviderEmployeeService verificationProviderEmployeeService;
    @Mock
    private UserRepository userRepository;
    private final String userName = "oles";
    @Mock
    User user;
    private boolean isOk = false;

    @Test
    public void oneProviderEmployeeTest() {
        VerificationProviderEmployeeService verifSer  = mock(VerificationProviderEmployeeService.class);
        when(verifSer.oneProviderEmployee(userName)).
                thenReturn(user);
        if (user == null) {
            isOk = true;
        }
        Assert.assertTrue(isOk);
    }


}
