package com.softserve.edu.service;

import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.calibrator.impl.CalibratorServiceImpl;
import javafx.beans.binding.Bindings;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;

/**
 * Created by Misha on 10/18/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalibratorServiceImplTest {

    @InjectMocks
    private CalibratorServiceImpl calibratorService;

    @Mock
    private OrganizationRepository calibratorRepository;

    @Before
    public void setUp() {
        calibratorService = new CalibratorServiceImpl();
    }

    @Test(expected = Exception.class)
    public void testfindByDistrict_excaption() {
        when(calibratorRepository.findByDistrictAndType(anyString(), anyString())).thenThrow(new Exception());
        calibratorService.findByDistrict("1", "1");
    }

}
