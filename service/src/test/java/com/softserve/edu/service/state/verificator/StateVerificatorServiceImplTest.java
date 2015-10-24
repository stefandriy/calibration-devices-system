package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.state.verificator.impl.StateVerificatorServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StateVerificatorServiceImplTest {

    final static String district = "district";
    final static String type = "type";
    final static Long id = 1L;

    @InjectMocks
    private StateVerificatorService stateVerificatorService = new StateVerificatorServiceImpl();

    @Mock
    private OrganizationRepository stateVerificatorRepository;

    @Mock
    private Organization cstateVerificatorlibrator;

    @Mock
    List<Organization> listOrganizations;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveStateVerificator() throws Exception {
        stateVerificatorService.saveStateVerificator(cstateVerificatorlibrator);
        verify(stateVerificatorRepository).save(cstateVerificatorlibrator);
    }

    @Test
    public void testFindById() throws Exception {
        when(stateVerificatorRepository.findOne(id)).thenReturn(cstateVerificatorlibrator);
        Assert.assertEquals(cstateVerificatorlibrator, stateVerificatorService.findById(id));
    }
}
