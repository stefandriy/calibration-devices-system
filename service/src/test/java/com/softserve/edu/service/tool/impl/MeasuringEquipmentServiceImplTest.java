package com.softserve.edu.service.tool.impl;

import com.softserve.edu.entity.device.MeasuringEquipment;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.MeasuringEquipmentRepository;
import com.softserve.edu.service.admin.impl.StatisticServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.stub;

/**
 * Created by Sonka on 21.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class MeasuringEquipmentServiceImplTest {
    private MeasuringEquipment measuringEquipment; // is used for editMeasuringEquipment
    private MeasuringEquipment measuringEquipment2; // is used for addMeasuringEquipment
    @Mock
    private MeasuringEquipmentRepository measuringEquipmentRepository;
    @InjectMocks
    private MeasuringEquipmentServiceImpl measuringEquipmentServiceImpl;

    @Before
    public void initializeMockito() {
        MockitoAnnotations.initMocks(this);
        measuringEquipment = new MeasuringEquipment(1L,"name1","manufacturer1");
        measuringEquipmentServiceImpl.addMeasuringEquipment(measuringEquipment);
        measuringEquipment2 =new MeasuringEquipment(2L,"name2","manufacturer2");
        stub(measuringEquipmentRepository.findOne(1L)).toReturn(measuringEquipment);
    }

    @After
    public void tearDown()  {
        measuringEquipmentServiceImpl=null;
        measuringEquipment = null;
        measuringEquipment2 = null;
    }

    @Test
    public void testGetAll() {

    }

    @Test
    public void testGetMeasuringEquipmentsBySearchAndPagination() {

    }

    @Test
    public void testAddMeasuringEquipment()  {
        boolean result;
        try {
            measuringEquipmentServiceImpl.addMeasuringEquipment(measuringEquipment2);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }

    @Test
    public void testGetMeasuringEquipmentById()  {
        MeasuringEquipment factual = measuringEquipmentServiceImpl.getMeasuringEquipmentById(1L);
        assertEquals(factual, measuringEquipment);
    }

    @Test
    public void testEditMeasuringEquipment()  {
      boolean result;
        try {
            measuringEquipmentServiceImpl.editMeasuringEquipment(1L,"name1","deviceType 1","manufacturer1.1","verificationInterval");
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }

    @Test
    public void testDeleteMeasuringEquipment()  {
        boolean result;
        try {
            measuringEquipmentServiceImpl.deleteMeasuringEquipment(1L);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }
}