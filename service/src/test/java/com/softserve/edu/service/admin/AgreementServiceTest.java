package com.softserve.edu.service.admin;

import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import com.softserve.edu.service.tool.DeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class AgreementServiceTest {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private DeviceService deviceService;

    @Test
    public void testAdd() {
        // Organization customer = organizationService.getOrganizationById(1L);
        // Organization executor = organizationService.getOrganizationById(1L);
        String number = "A 123";
        Long deviceCount = 33L;
        Device.DeviceType deviceType = Device.DeviceType.WATER;
        Date date = new Date();
        Agreement newAgreement = agreementService.add(1L, 1L, number, deviceCount, date, deviceType);

        Set<Agreement> agreements = agreementService.findAll();
        assertEquals(number, newAgreement.getNumber());
        assertTrue(agreements.contains(newAgreement));
    }

    @Test
    public void testFindByCustomerIdAndDeviceType() {
        Set<Agreement> agreements = agreementService.findByCustomerIdAndDeviceType(1L, Device.DeviceType.WATER);
        assertTrue(!agreements.isEmpty());
    }
}
