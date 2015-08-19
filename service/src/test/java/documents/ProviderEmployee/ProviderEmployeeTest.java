package documents.ProviderEmployee;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.verification.VerificationProviderEmployeeService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


public class ProviderEmployeeTest {

	@InjectMocks
    private VerificationProviderEmployeeService verificationProviderEmployeeService;
    @Mock
    private UserRepository userRepository;
    private final String userName = "oles";
    @Mock
    User user;
    private boolean isOk = false;
    
    @Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

    /**
     * Fixed by Nazariii
     */
    @Test
    public void oneProviderEmployeeTest() {
    	ArgumentCaptor<String> userNameArg = ArgumentCaptor.forClass(String.class);
    	verificationProviderEmployeeService.oneProviderEmployee(userName);
        verify(userRepository).getUserByUserName(userNameArg.capture());

        Assert.assertEquals(userName, userNameArg.getValue());
    }


}
