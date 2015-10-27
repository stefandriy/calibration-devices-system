package com.softserve.edu.service.provider;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.provider.impl.ProviderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProviderServiceImplTest {

	@InjectMocks
	private ProviderService providerService = new ProviderServiceImpl();

	@Mock
	private OrganizationRepository mockProviderRepository;

	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	/*@Test
	public void testFindByDistrictParametersPropagation() {
		final String district = "district"; 
		final String type = "type";
		final Organization organization = Mockito.mock(Organization.class);
		final List<Organization> organizations = Collections.singletonList(organization);
		
		when(mockProviderRepository.findByDistrictAndType(anyString(), anyString())).thenReturn(organizations);
		
		providerService.findByDistrictAndType(district, type);
		
		ArgumentCaptor<String> distinctArg = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> typeArg = ArgumentCaptor.forClass(String.class);
		
		verify(mockProviderRepository).findByDistrictAndType(distinctArg.capture(), typeArg.capture());
		
		Assert.assertEquals(district, distinctArg.getValue());
		Assert.assertEquals(type, typeArg.getValue());
		Assert.assertEquals(organizations, providerService.findByDistrictAndType(district, type));
	}*/

	@Test
    public void testFindByIdParametersPropagation() {
		final Long finalId = 1L;
		Organization mockOrganization = mock(Organization.class);
		
		when(mockProviderRepository.findOne(anyLong())).thenReturn(mockOrganization);
		
		providerService.findById(finalId);
		ArgumentCaptor<Long> finalIdArg = ArgumentCaptor.forClass(Long.class);
		
		verify(mockProviderRepository).findOne(finalIdArg.capture());
	
		Assert.assertEquals(finalId, finalIdArg.getValue());
		Assert.assertEquals(mockOrganization, providerService.findById(finalId));
    }
}
