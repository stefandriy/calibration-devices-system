package provider;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;

public class ProviderServiceTest {

	@InjectMocks
	private ProviderServiceTest providerServiceTest;

	@Mock
	private OrganizationRepository providerRepository;

	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testfindByDistrict() {
		List<Organization> mockList = mock(List.class);
		when(this.providerRepository.getByTypeAndDistrict(anyString(),
								   anyString())).thenReturn(mockList);

		verify(mockList).get(anyInt());
	}

	
	
}
