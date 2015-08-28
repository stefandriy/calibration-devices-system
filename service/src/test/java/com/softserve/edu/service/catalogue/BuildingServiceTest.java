package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Building;
import com.softserve.edu.repository.catalogue.BuildingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Volodya NT on 18.08.2015.
 */
public class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;
    @InjectMocks
    BuildingService buildingService;

    //@Mock
    //Building building;
//    @Rule
//    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void testGetBuildingsCorrespondingStreet() throws Exception {
        List<Building> b = mock(ArrayList.class);

        final Long streetId = 11l;
        when(buildingRepository.findByStreetId(112l)).thenThrow(new Exception());
        b = buildingRepository.findByStreetId(112l);



        ArgumentCaptor<Long> streetIdArgumentCapture = ArgumentCaptor.forClass(Long.class);
        buildingRepository.findByStreetId(streetId);
        verify(buildingRepository).findByStreetId(streetIdArgumentCapture.capture());
        Assert.assertEquals(streetId, streetIdArgumentCapture.getValue());

        when(buildingRepository.findByStreetId(anyLong())).thenReturn(b);
        Assert.assertEquals(b, buildingRepository.findByStreetId((long) 1));


    }
}