package com.softserve.edu.service.verification;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.springframework.data.domain.*;

import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerificationServiceTest {
	@InjectMocks
	private VerificationService verificationService;

	@Mock
	VerificationRepository verificationRepository;
	
	@Mock
	CalibrationTestRepository calibrationTestRepository;
	
	@Mock
	Verification verification;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test1() {
		when(verificationRepository.findOne("123")).thenReturn(new Verification());
		Verification get=verificationService.findById("123");
		assertEquals(get,new Verification()); 
	}
	
	@Test
	public void test2() {
		when(calibrationTestRepository.findById(-1L)).thenReturn(null);
		try{
			verificationService.findByCalibrationTestId(-1L);
		}
		catch(AccessDeniedException exc){
			System.out.println("Exception was thrown");
		}
	}

}
