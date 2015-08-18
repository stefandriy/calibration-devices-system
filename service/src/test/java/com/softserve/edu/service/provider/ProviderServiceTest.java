package com.softserve.edu.service.provider;

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
		
		verify(mockProviderRepository).getByTypeAndDistrict(distinctArg.capture(), typeArg.capture());
		
		Assert.assertEquals(district, distinctArg.getValue()); 
		Assert.assertEquals(type, typeArg.getValue());
		Assert.assertEquals(organizations, providerService.findByDistrict(district, type));
	}
	
	@Test
	public void testGetTypesByIdParametersPropagation() {
		final Long id = 1L;
		final Set<String> mockSet = mock(Set.class);
		
		when(mockProviderRepository.getOrganizationTypesById(anyLong())).thenReturn(mockSet);
		
		providerService.getTypesById(id);
		ArgumentCaptor<Long> idArg = ArgumentCaptor.forClass(Long.class);
		
		verify(mockProviderRepository).getOrganizationTypesById(idArg.capture());
		
		Assert.assertEquals(id, idArg.getValue());
		Assert.assertEquals(mockSet, providerService.getTypesById(id));
    }
	
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
