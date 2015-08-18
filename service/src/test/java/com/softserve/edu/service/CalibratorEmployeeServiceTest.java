package com.softserve.edu.service;

import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CalibratorEmployeeServiceTest {
	@Mock private UserRepository calibratorEmployeeRepository;
	@Mock private VerificationRepository verificationRepository;
	@InjectMocks private CalibratorEmployeeService calibratorEmployeeService;
	
	@Mock User calibratorEmployee;
	private String name = "somename";
	
	@Test
	public void testOneCalibratorEmployee (){
		calibratorEmployeeService.oneCalibratorEmployee(name);
		verify(calibratorEmployeeRepository).getUserByUserName(name);
	}
	
	@Test
	public void testGetVerificationListbyCalibratorEmployee (){
		calibratorEmployeeService.getVerificationListbyCalibratorEmployee(name);
		verify(verificationRepository).findByCalibratorEmployeeUsernameAndStatus(name, Status.ACCEPTED);
		
	}
	
	@Test
	public void testAddEmployee (){
		// TODO
	}

}
