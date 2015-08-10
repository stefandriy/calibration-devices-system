package com.softserve.edu.service.utils;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.MailService;


public class ProcessTimeChecker {
	
	@Autowired
	private  MailService mailService;
	
	@Autowired
	private  VerificationRepository verificationRepository;
	
	private static  final long DAY_LENGTH_MILLIS = 24 * 60 * 60 * 1000; 
	
	@Scheduled(fixedDelay = DAY_LENGTH_MILLIS)
	public void runProcessTimeCheck() {
		System.out.println("scheduler running");
		List<Verification> verificationList = (List<Verification>) verificationRepository.findAll();
		int processTimeExceeding = 0;
		String mailTo = null;
		int maxProcessTime = 0;
		for (Verification verification : verificationList) {
			if ((verification.getStatus().equals(Status.SENT)) || (verification.getStatus().equals(Status.ACCEPTED))) {
				processTimeExceeding = ProcessTimeChecker.processTimeForProvider(verification);
				mailTo = ProcessTimeChecker.resolveMailToProvider(verification);
				maxProcessTime = verification.getProvider().getMaxProcessTime();
			} else if ((verification.getStatus().equals(Status.IN_PROGRESS)) || (verification.getStatus().equals(Status.TEST_PLACE_DETERMINED)) || (verification.getStatus().equals(Status.SENT_TO_TEST_DEVICE)) || (verification.getStatus().equals(Status.TEST_COMPLETED))) {
				processTimeExceeding = ProcessTimeChecker.processTimeForCalibrator(verification);
				mailTo = ProcessTimeChecker.resolveMailToCalibrator(verification);
				maxProcessTime = verification.getCalibrator().getMaxProcessTime();
			}  else if ((verification.getStatus().equals(Status.SENT_TO_VERIFICATOR)) || (verification.getStatus().equals(Status.TEST_OK)) || (verification.getStatus().equals(Status.TEST_NOK))) {
				processTimeExceeding = ProcessTimeChecker.processTimeForVerificator(verification);
				mailTo = ProcessTimeChecker.resolveMailToVerificator(verification);
				maxProcessTime = verification.getStateVerificator().getMaxProcessTime();
			} 
			
			if (processTimeExceeding > 0) {
				verification.setProcessTimeExceeding(processTimeExceeding);
				verificationRepository.save(verification);
				mailService.sendTimeExceededMail (verification.getId(), processTimeExceeding, maxProcessTime, mailTo);				
			}
		}
	}
	
	private static int processTimeForProvider(Verification verification) {
		DateTime today = new DateTime(new Date());
		int maxProcessTime = verification.getProvider().getMaxProcessTime();
		if (verification.getStatus().equals(Status.SENT)) {
			int differenceInDays = Days.daysBetween( new DateTime( verification.getInitialDate()), today).getDays();
			if (differenceInDays > maxProcessTime) {
				int processTimeExceeding = differenceInDays - maxProcessTime;
				return processTimeExceeding;				
			} 
		} else if (verification.getStatus().equals(Status.ACCEPTED)) {
			int differenceInDays = Days.daysBetween( new DateTime( verification.getExpirationDate()), today).getDays();
			if (differenceInDays > maxProcessTime) {
				int processTimeExceeding = differenceInDays - maxProcessTime;
				return processTimeExceeding;				
			} 
		}
		return 0;
	}
	
	private static int processTimeForCalibrator(Verification verification) {
		DateTime today = new DateTime(new Date());
		int differenceInDays = Days.daysBetween(new DateTime(verification.getExpirationDate()), today).getDays();
		int maxProcessTime = verification.getCalibrator().getMaxProcessTime();
		if (differenceInDays > maxProcessTime) {
			int processTimeExceeding = differenceInDays - maxProcessTime;
			return processTimeExceeding;
		}
		return 0;
	}

	private static int processTimeForVerificator(Verification verification) {
		DateTime today = new DateTime(new Date());
		int differenceInDays = Days.daysBetween(new DateTime(verification.getExpirationDate()), today).getDays();
		int maxProcessTime = verification.getStateVerificator().getMaxProcessTime();
		if (differenceInDays > maxProcessTime) {
			int processTimeExceeding = differenceInDays - maxProcessTime;
			return processTimeExceeding;
		}
		return 0;
	}
	
	private static String resolveMailToProvider(Verification verification) {
		String mailTo = (verification.getProviderEmployee() == null) ? verification.getProvider().getEmail() : verification.getProviderEmployee().getEmail();
		return mailTo;
	}
	
	private static String resolveMailToCalibrator(Verification verification) {
		String mailTo = (verification.getCalibratorEmployee() == null) ? verification.getCalibrator().getEmail() : verification.getCalibratorEmployee().getEmail();
		return mailTo;
	}
	
	private static String resolveMailToVerificator(Verification verification) {
		String mailTo = (verification.getStateVerificatorEmployee() == null) ? verification.getStateVerificator().getEmail() : verification.getStateVerificatorEmployee().getEmail();
		return mailTo;
	}
}
