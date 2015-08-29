package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.repository.OrganizationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by vova on 25.08.15.
 */
public class StateVerificatorServiceTest {

    final static String district = "district";
    final static String type = "type";
    final static Long id = 1L;

    @InjectMocks
    private StateVerificatorService stateVerificatorService =new StateVerificatorService();

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
    public void testFindByDistrict() throws Exception {
        when(stateVerificatorRepository.getByTypeAndDistrict(district, type)).thenReturn(listOrganizations);
        Assert.assertEquals(listOrganizations, stateVerificatorService.findByDistrict(district, type));
    }

    @Test
    public void testFindById() throws Exception {
        when(stateVerificatorRepository.findOne(id)).thenReturn(cstateVerificatorlibrator);
        Assert.assertEquals(cstateVerificatorlibrator, stateVerificatorService.findById(id));
    }
}