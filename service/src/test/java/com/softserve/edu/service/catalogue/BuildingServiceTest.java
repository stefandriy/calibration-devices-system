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

import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Created by Volodya NT on 18.08.2015.
 */
public class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;
    @InjectMocks
    BuildingService buildingService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBuildingsCorrespondingStreet() throws Exception {
        List<Building> b = mock(List.class);
        final Long streetId = 11l;

        ArgumentCaptor<Long> streetIdArgumentCapture = ArgumentCaptor.forClass(Long.class);
        buildingService.getBuildingsCorrespondingStreet(streetId);
        verify(buildingRepository).findByStreetId(streetIdArgumentCapture.capture());
        Assert.assertEquals(streetId, streetIdArgumentCapture.getValue());
        verify(buildingRepository, times(1)).findByStreetId(streetId);

        when(buildingRepository.findByStreetId(anyLong())).thenReturn(b);
        Assert.assertEquals(b, buildingService.getBuildingsCorrespondingStreet((long) 1));
    }
}