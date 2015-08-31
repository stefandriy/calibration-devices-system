package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.repository.catalogue.DistrictRepository;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Volodya NT on 18.08.2015.
 */
public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;
    @InjectMocks
    DistrictService districtService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDistrictsCorrespondingRegion() throws Exception {
        List<District> b = mock(ArrayList.class);
        final Long streetId = 1L;


        ArgumentCaptor<Long> streetIdArgumentCapture = ArgumentCaptor.forClass(Long.class);
        districtService.getDistrictsCorrespondingRegion(streetId);
        verify(districtRepository).findByRegionId(streetIdArgumentCapture.capture());
        Assert.assertEquals(streetId, streetIdArgumentCapture.getValue());

        when(districtRepository.findByRegionId(anyLong())).thenReturn(b);
        Assert.assertEquals(b, districtService.getDistrictsCorrespondingRegion(1L));
    }

    @Test
    public void testFindDistrictByDesignationAndRegion() throws Exception {
        District district = mock(District.class);
        final Long region = 5L;
        final String destination = "dest";

        ArgumentCaptor<Long> streetIdArgumentCapture = ArgumentCaptor.forClass(Long.class);
        districtService.findDistrictByDesignationAndRegion(destination, region);
        verify(districtRepository).findByRegionId(streetIdArgumentCapture.capture());
        Assert.assertEquals(region, streetIdArgumentCapture.getValue());
        Assert.assertEquals(destination, streetIdArgumentCapture.getValue());
        verify(districtRepository, times(1)).save(district);
        when(districtRepository.findByDesignationAndRegionId(anyString(), anyLong())).thenReturn(district);
        Assert.assertEquals(district,districtService.findDistrictByDesignationAndRegion("dest", (long) 11));
    }
}