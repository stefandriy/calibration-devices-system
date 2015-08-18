package provider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.provider.ProviderService;

public class ProviderServiceTest {

	@InjectMocks
	private ProviderService providerService;

	@Mock
	private OrganizationRepository mockProviderRepository;

	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testFindByDistrictParametersPropagation() {
		final String district = "district"; 
		final String type = "type";
		final Organization organization = Mockito.mock(Organization.class);
		final List<Organization> organizations = Collections.singletonList(organization);
		
		when(mockProviderRepository.getByTypeAndDistrict(anyString(), anyString())).thenReturn(organizations);
		
		providerService.findByDistrict(district, type);
		
		ArgumentCaptor<String> distinctArg = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> typeArg = ArgumentCaptor.forClass(String.class);
		
		Mockito.verify(mockProviderRepository).getByTypeAndDistrict(distinctArg.capture(), typeArg.capture());
		
		Assert.assertEquals(district, distinctArg.getValue()); 
		Assert.assertEquals(type, typeArg.getValue());
		Assert.assertEquals(organizations, providerService.findByDistrict(district, type));
	}

	//@Test
	//public void testFindByDistrictWithException()
	
	
}
