package com.softserve.edu.service.verification;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationServiceImplTest {
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
		Verification get= verificationService.findById("123");
		assertEquals(new Verification(),get);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void test2() {
		when(calibrationTestRepository.findById(-1L)).thenReturn(null);
		verificationService.findByCalibrationTestId(-1L);
	}
	

	@Test
	public void test3() {
		Verification a=new Verification();
		verificationService.saveVerification(a);
		verify(verificationRepository).save(a);
	}
}