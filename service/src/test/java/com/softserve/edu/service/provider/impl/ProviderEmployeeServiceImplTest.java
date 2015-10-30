package com.softserve.edu.service.provider.impl;

import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.utils.EmployeeDTO;
import junit.framework.TestCase;
import org.junit.Assert;
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

//    @Test
//    public void testGetAllProviderEmployee() throws Exception {
//        final Long organizationId = 43L;
//        List<String> actualProviderEmployeeNameList = providerEmployeeService.getAllProviderEmployee(organizationId)
//                .stream()
//                .map(User::getUsername)
//                .collect(Collectors.toList());
//        String[] expectProviderEmployeeNameList = {
//                "lv_vodo_kanal_employee", "mikko", "olenka", "oles",
//                "taras", "vaska", "volodka", "volodya_pr"
//        };
//        List<String> expectList = Arrays.asList(expectProviderEmployeeNameList);
//        Assert.assertEquals(expectList, actualProviderEmployeeNameList);
//    }


    /**
     * if user has role ADMIN, function will return list with his employees
     * else if user has EMPLOYEE role, function will return only current user
     *  Testing findAllAvailableUsersByRoleAndOrganizationId from repository
     */
//    @Test
//    public void testGetAllProviders() {
//        //If user has role PROVIDER_EMPLOYEE
//        User providerEmployee = providerEmployeeService.oneProviderEmployee("volodya_pr");
//        List<String> role = providerEmployee.getUserRoles()
//                .stream()
//                .map(Enum::name)
//                .collect(Collectors.toList());
//        List<EmployeeDTO> providerList = providerEmployeeService.getAllProviders(role, providerEmployee);
//        Assert.assertEquals("volodya_pr", providerList.iterator().next().getUsername());
//        try {
//            providerList.get(1);
//            Assert.assertTrue(false);
//        } catch(IndexOutOfBoundsException e) {
//            Assert.assertTrue(true);
//        }
//
//        //if user has role PROVIDER_ADMIN
//        User providerAdmin = providerEmployeeService.oneProviderEmployee("provider-lv");
//        List<String> roleAdmin = providerAdmin.getUserRoles() /*return Set<String>*/
//                .stream()
//                .map(Enum::name)
//                .collect(Collectors.toList());
//        List<EmployeeDTO> providers = providerEmployeeService.getAllProviders(roleAdmin, providerAdmin);
//
//        List<String> actualList = providers.stream()/*return List<EmployeeDTO>*/
//                .map(EmployeeDTO::getUsername)
//                .collect(Collectors.toList());
//
//        String[] expectProviderEmployeeNameList = {
//                "lv_vodo_kanal_employee", "mikko", "olenka", "oles",
//                "taras", "vaska", "volodka", "volodya_pr"
//        };
//
//        List<String> expectList = Arrays.asList(expectProviderEmployeeNameList);
//        Assert.assertEquals(expectList, actualList);
//
//    }

}