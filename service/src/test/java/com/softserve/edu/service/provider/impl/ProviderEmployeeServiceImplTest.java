package com.softserve.edu.service.provider.impl;

import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Volodya NT on 08.10.2015.
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class ProviderEmployeeServiceImplTest extends TestCase {

    @Autowired
    ProviderEmployeeService providerEmployeeService;
    /**
     * find all provider employee
     * checking repository query
     * findByUserRoleAndOrganizationId()
     * @throws Exception
     */

    @Test
    public void testGetAllProviderEmployee() throws Exception {
        final Long organizationId = 43L;
        List<String> actualProviderEmployeeNameList = providerEmployeeService.getAllProviderEmployee(organizationId)
                .stream()
                .map(User::getUsername)//user::getUserName
                .collect(Collectors.toList());
        String[] expectProviderEmployeeNameList = {
                "lv_vodo_kanal_employee","mikko","newman",
                "olenka","oles","taras","vaska","volodka"
        };
        List<String> expectList = Arrays.asList(expectProviderEmployeeNameList);
        Assert.assertEquals(expectList, actualProviderEmployeeNameList);
    }

}