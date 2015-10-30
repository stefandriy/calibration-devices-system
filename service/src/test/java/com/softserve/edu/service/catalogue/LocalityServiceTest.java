package com.softserve.edu.service.catalogue;


import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.catalogue.Locality;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class LocalityServiceTest {
/*
    @Autowired
    private LocalityService localityService;

    @Test
    public void testFindOrganizationByLocalityId() {
        List<String> list = localityService.getMailIndexForLocality("Київ", 576L);

        assertTrue(!list.isEmpty());
    }

    @Test
    public void testGetLocalitiesCorrespondingDistrict() {
        List<Locality> list = localityService.getLocalitiesCorrespondingDistrict(1L);

        assertTrue(!list.isEmpty());
    }

    @Test
    public void testFindByDistrictIdAndOrganizationId() {
        List<Locality> list = localityService.findByDistrictIdAndOrganizationId(567L,1L);

        assertTrue(!list.isEmpty());
    }
**/
}
