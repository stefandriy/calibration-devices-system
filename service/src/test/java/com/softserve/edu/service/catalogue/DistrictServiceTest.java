package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.repository.catalogue.DistrictRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Volodya NT on 18.08.2015.
 */
public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;
    @InjectMocks
    DistrictService districtService;
    //@Mock
    //Building building;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDistrictsCorrespondingRegion() throws Exception {
        List<District> b = mock(ArrayList.class);
        when(districtRepository.findByRegionId(anyLong())).thenReturn(b);
        Assert.assertEquals(b, districtRepository.findByRegionId((long) 1));
    }

    @Test
    public void testFindDistrictByDesignationAndRegion() throws Exception {
        District district = mock(District.class);
        when(districtRepository.findByDesignationAndRegionId(anyString(),anyLong())).thenReturn(district);
        Assert.assertEquals(district,districtRepository.findByDesignationAndRegionId("dest", (long) 11));
    }
}