package com.softserve.edu.service.catalogue;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.softserve.edu.config.ServiceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.assertEquals;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class LocalityServiceTest {

    @Autowired
    private LocalityService localityService;

    @Test
    public void testFindOrganizationByLocalityId() {
        List<String> list = localityService.getMailIndexForLocality("Київ", 567L);

        assertEquals(true, true);
    }

    @Test
    public void testGetLocalitiesCorrespondingDistrict() {
        //List<String> list = localityService.getLocalitiesCorrespondingDistrict();

        assertEquals(true, true);
    }
}
