package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.utils.filter.Filter;
import com.softserve.edu.service.utils.filter.internal.Comparison;
import com.softserve.edu.service.utils.filter.internal.Condition;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * Created by Pavlo on 12.11.2015.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CalibrationModuleServiceImpl.class, Filter.class, CalibrationModuleRepository.class})
//@RunWith(MockitoJUnitRunner.class)
public class CalibrationModuleServiceImplTest {
    @Mock
    CalibrationModuleRepository calibrationModuleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CalibrationModule calibrationModule;
    @Mock
    Pageable pageable;
    @Mock
    Filter filter;
    @Mock
    User user;
    @Mock
    Page<CalibrationModule> calibrationModulePage;
    @Mock
    List<CalibrationModule> calibrationModulesList;
    @Mock
    Organization organization;
    @Mock
    Logger logger;
    @Mock
    Filter.FilterBuilder filterBuilder;
    private Long id;

    @InjectMocks
    CalibrationModuleServiceImpl calibrationModuleService;

    @Before
    public void setUp() throws Exception {
        when(calibrationModuleRepository.save(calibrationModule)).thenReturn(calibrationModule);
        when(calibrationModuleRepository.findOne(id)).thenReturn(calibrationModule);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void testAddCalibrationModuleCatchException() throws Exception {
        thrown.expect(NullPointerException.class);
        calibrationModuleService.addCalibrationModule(null);
    }

    @Test
    public void testAddCalibrationModule() {
        when(calibrationModuleRepository.saveWithGenerating(calibrationModule)).thenReturn(calibrationModule);
        CalibrationModule expected = calibrationModule;
        CalibrationModule actual = calibrationModuleService.addCalibrationModule(calibrationModule);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindModuleById() throws Exception {
        CalibrationModule expected = calibrationModule;
        CalibrationModule actual = calibrationModuleService.findModuleById(id);
        assertEquals(expected, actual);
    }

    @Test
    public void testDisableCalibrationModule() throws Exception {
        calibrationModuleService.disableCalibrationModule(id);
        assertFalse(calibrationModuleService.findModuleById(id).getIsActive());
    }

    @Test
    public void testGetFilteredPageOfCalibrationModule() throws Exception {
        urn(filter);
        PowerMockito.whenNew(Filter.FilterBuilder.class).withNoArguments().thenReturn(filterBuilder);
        Map<String, Object> searchKeys = new HashMap<>();
        searchKeys.put("isActive", "true");
        searchKeys.put("employeeFullName", "fullName");
        when(filterBuilder.setSearchMap(searchKeys)).thenReturn(filterBuilder);
        when(filterBuilder.setSearchMap(searchKeys).build()).thenReturn(filter);
        when(calibrationModuleRepository.findAll(filter, pageable)).thenReturn(calibrationModulePage);
        Page<CalibrationModule> expected = calibrationModulePage;
        Page<CalibrationModule> actual = calibrationModuleService.getFilteredPageOfCalibrationModule(searchKeys, pageable);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllModules() throws Exception {
        when(calibrationModuleRepository.findAll(pageable)).thenReturn(calibrationModulePage);
        Page expected = calibrationModulePage;
        Page actual = calibrationModuleService.findAllModules(pageable);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCalibrationModule() throws Exception {
        calibrationModuleService.updateCalibrationModule(id, calibrationModule);
        verify(calibrationModule).updateFields(calibrationModule);
    }

    @Test
    public void testFindAllCalibrationModulsNumbers() throws Exception {
        String moduleType = "INSTALLATION_FIX";
        Date workDate = new Date();
        String deviceType = "WATER";
        String username = "username";
        String serialNumber = "serialNumber";
        Long organizationId = 100L;
        List<Condition> conditions = new ArrayList<>();
        List<CalibrationModule> modules = new ArrayList<>();
        modules.add(calibrationModule);
        when(user.getOrganization()).thenReturn(organization);
        when(user.getOrganization().getId()).thenReturn(organizationId);
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.like).setField("moduleType").setValue(CalibrationModule.ModuleType.valueOf(moduleType)).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("workDate").setValue(workDate).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("deviceType").setValue(Device.DeviceType.valueOf(deviceType)).build());
        conditions.add(new Condition.Builder()
                .setComparison(Comparison.eq).setField("organizationCode").setValue(user.getOrganization().getId())
                .build());
        filter.addConditionList(conditions);
        calibrationModulesList.add(calibrationModule);
        when(userRepository.findOne(username)).thenReturn(user);
        PowerMockito.whenNew(Filter.class).withNoArguments().thenReturn(filter);
        when(calibrationModuleRepository.findAll(filter)).thenReturn(modules);
        when(calibrationModule.getSerialNumber()).thenReturn(serialNumber);
        List<String> expected = new ArrayList<String>();
        expected.add(serialNumber);
        List<String> actual = calibrationModuleService.findAllCalibrationModulsNumbers(moduleType, workDate, deviceType, username);
        assertEquals(expected, actual);
    }

    @Test
    public void testfindAllCalibrationModulesNumbersCatchUserNullException() {
        thrown.expect(NullPointerException.class);
        String moduleType = "INSTALLATION_FIX";
        Date workDate = new Date();
        String applicationField = "applicationField";
        String username = "username";
        String serialNumber = "serialNumber";
        when(userRepository.findOne(username)).thenReturn(null);
        calibrationModuleService.findAllCalibrationModulsNumbers(moduleType, workDate, applicationField, username);
        verify(logger).error("Cannot found user!");
    }

    @Test
    public void testFindAllCalibrationModulesNumbersCatchNoModulesException() throws Exception {
        thrown.expect(NullPointerException.class);
        String moduleType = "INSTALLATION_FIX";
        Date workDate = new Date();
        String deviceType = "WATER";
        String username = "username";
        Long organizationId = 100L;
        when(user.getOrganization()).thenReturn(organization);
        when(user.getOrganization().getId()).thenReturn(organizationId);
        when(userRepository.findOne(username)).thenReturn(user);
        PowerMockito.whenNew(Filter.class).withNoArguments().thenReturn(filter);
        when(calibrationModuleRepository.findAll(filter)).thenReturn(null);
        calibrationModuleService.findAllCalibrationModulsNumbers(moduleType, workDate, deviceType, username);
        verify(logger).error("Cannot found modules for the choosen workDate " + workDate);
    }
}