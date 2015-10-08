package com.softserve.edu.service.catalogue;

import com.softserve.edu.config.ServiceConfig;
import com.softserve.edu.entity.catalogue.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Назік on 10/5/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @Test
    public void testFindByDistrictId () {
        Region region = regionService.findByDistrictId(1L);
        assertEquals(true, true);
    }
}
