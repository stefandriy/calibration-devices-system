package com.softserve.edu.service.utils;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.MailService;
import com.softserve.edu.service.verification.VerificationService;


public class ProcessTimeChecker {
	
	@Autowired
	private  MailService mailService;
	
	@Autowired
	private  VerificationRepository verificationRepository;
	@Autowired
	private  VerificationService verificationService;


//	@Scheduled(fixedRate=50000)
	@Scheduled(cron="0 0 23 * * *")
	public void runProcessTimeCheck() {		
		List<Object[]> providerList = verificationService.getProcessTimeProvider();
		processTime(providerList);
		List<Object[]> calibratorList = verificationService.getProcessTimeCalibrator();
		processTime(calibratorList);
		List<Object[]> verificatorList = verificationService.getProcessTimeVerificator();
		processTime(verificatorList);

	}
	
	private  void processTime(List<Object[]> list) {
		DateTime today = new DateTime(new Date());
		int processTimeExceeding = 0;
		for(Object[] array : list){
			
			int maxProcessTime = (int) array[2];
			int differenceInDays = Days.daysBetween( new DateTime((Date) array[0]), today).getDays();
			if (differenceInDays > maxProcessTime) {
				processTimeExceeding = differenceInDays - maxProcessTime;
				Verification verification = verificationRepository.findOne((String) array[1]);
				verification.setProcessTimeExceeding(processTimeExceeding);
				verificationRepository.save(verification);
				mailService.sendTimeExceededMail (verification.getId(), processTimeExceeding, maxProcessTime, (String) array[3]);
			}
		}
		
	}

}
