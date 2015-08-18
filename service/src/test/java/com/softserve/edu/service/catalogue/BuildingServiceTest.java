package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Building;
import com.softserve.edu.repository.catalogue.BuildingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetBuildingsCorrespondingStreet() throws Exception {
        List<Building> b = mock(ArrayList.class);
        //How create exact data and try to match with actual data?
        //How create real Building object?
        when(buildingRepository.findByStreetId(anyLong())).thenReturn(b);
        Assert.assertEquals(b, buildingRepository.findByStreetId((long) 1));
    }
}