package com.softserve.edu.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.softserve.edu.entity.MeasuringEquipment;
import com.softserve.edu.repository.MeasuringEquipmentRepository;
import com.softserve.edu.service.MeasuringEquipmentService;

import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MeasuringEquipmentServiceTest {
	@InjectMocks
	private MeasuringEquipmentService mes;
	
	@Mock
	MeasuringEquipmentRepository measuringEquipmentRepository;
	
	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test1() {
		MeasuringEquipment me=new MeasuringEquipment(123L,"Equi","Samsung");
		when(measuringEquipmentRepository.findOne(123L)).thenReturn(me);
		MeasuringEquipment got=mes.deleteMeasuringEquipment(123L);
		assertEquals(me,got);
	}
	
	@Test
	public void test2(){
		ArgumentCaptor<Long> par1 = ArgumentCaptor.forClass(Long.class);
		mes.deleteMeasuringEquipment(123L);
		verify(measuringEquipmentRepository).findOne(par1.capture());
		assertEquals((Long)123L, par1.getValue());
	}
}