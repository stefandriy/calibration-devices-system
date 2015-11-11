package com.softserve.edu.service.admin;

import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Agreement;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class AgreementServiceTest {

    @Autowired
    private AgreementService agreementService;

    /**
     * Saves agreement then test if all parameters are saved.
     */
    @Test
    public void testAdd() {
        String agreementNumber = "A 123";
        int deviceCount = 33;
        Device.DeviceType deviceType = Device.DeviceType.WATER;
        Date date = new Date();
        Long customerId = 1L;
        Long executorId = 1L;
        Agreement savedAgreement = agreementService.add(customerId, executorId, agreementNumber, deviceCount, date, deviceType);

        Agreement agreementFromDB = agreementService.findAgreementById(savedAgreement.getId());
        assertEquals(deviceCount, agreementFromDB.getDeviceCount());
        assertEquals(agreementNumber, agreementFromDB.getNumber());
        assertEquals(deviceType, agreementFromDB.getDeviceType());
        assertEquals(customerId, agreementFromDB.getCustomer().getId());
        assertEquals(executorId, agreementFromDB.getExecutor().getId());

    }

    /**
     * Find agreement by customer id and device type and then check if device type and customer are correct.
     */
    @Test
    public void testFindByCustomerIdAndDeviceType() {
        Long customerId = 1L;
        Device.DeviceType deviceType = Device.DeviceType.WATER;
        Set<Agreement> agreements = agreementService.findByCustomerIdAndDeviceType(customerId, deviceType);

        assertTrue(!agreements.isEmpty());
        Agreement firstAgreement = agreements.iterator().next();
        assertEquals(customerId, firstAgreement.getCustomer().getId());
        assertEquals(deviceType, firstAgreement.getDeviceType());
    }


}