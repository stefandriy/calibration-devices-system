package com.softserve.edu.service;


import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.util.CalibrationTestResult;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CalibrationTestService {

    @Autowired
    private CalibrationTestRepository testRepository;

    @Autowired
    private CalibrationTestDataRepository dataRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Transactional
    public CalibrationTest findTestById(Long testId){
        return testRepository.findById(testId);
    }

    @Transactional
    public CalibrationTest findByVerificationId(String verifId){
        return testRepository.findByVerificationId(verifId);
    }

    @Transactional
    public CalibrationTestList findAllCalibrationTests (){
    	List<CalibrationTest> list = (ArrayList<CalibrationTest>) testRepository.findAll();
    	CalibrationTestList foundTests = new CalibrationTestList(list);
    	return foundTests;
    }

    @Transactional
    public Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber,
                                                                                int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search == null ? testRepository.findAll(pageRequest)
                : testRepository.findByNameLikeIgnoreCase("%" + search + "%",
                pageRequest);
    }

//    @Transactional
//    public CalibrationTest createTest(CalibrationTest test){return testRepository.save(test);
//    }

   //IN PROGRESS!
    @Transactional
    public void createNewTest(CalibrationTest calibrationTest, String verificationId){
        Verification verification = verificationRepository.findOne(verificationId);
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);
    }

    @Transactional
    public CalibrationTest editTest(Long testId, String name, Date dateTest, Integer temperature, Integer settingNumber,
    		Double latitude, Double longitude, String consumptionStatus){
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        calibrationTest.setName(name);
        calibrationTest.setDateTest(dateTest);
        calibrationTest.setTemperature(temperature);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(consumptionStatus);
        return calibrationTest;
    }

    @Transactional
    public CalibrationTest deleteTest(Long testId){
    	CalibrationTest deletedCalibrationTest = testRepository.findOne(testId);
    	testRepository.delete(testId);
    	return deletedCalibrationTest;
    }
    
    //TestData
    @Transactional
    public CalibrationTestData createTestData(Long testId, CalibrationTestData data) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        if(calibrationTest == null) {
            throw new NotAvailableException();
        }
        CalibrationTestData testData = dataRepository.save(data);
        testData.setCalibrationTest(calibrationTest);
        return testData;
    }

    @Transactional
    public CalibrationTestDataList findAllTestDataAsociatedWithTest(Long calibrationTestId){
        CalibrationTest calibrationTest = testRepository.findOne(calibrationTestId);
        if (calibrationTest == null){
            throw new NotAvailableException("Тесту з таким ID не існує!");
        }else{
            return new CalibrationTestDataList(calibrationTestId
                    , dataRepository.findByCalibrationTestId(calibrationTestId));
        }
    }
}