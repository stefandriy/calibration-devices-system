package com.softserve.edu.service;

import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that implements CRUD operations for test data.
 */
@Service
public class CalibrationTestDataService {

    @Autowired
    private CalibrationTestDataRepository dataRepository;

    @Transactional
    public CalibrationTestData findTestData(Long id){
        return dataRepository.findOne(id);
    }

    @Transactional
    public CalibrationTestData deleteTestData(Long id){
        CalibrationTestData deletedTestData = dataRepository.findOne(id);
        dataRepository.delete(id);
        return deletedTestData;
    }

    @Transactional
    public CalibrationTestData editTestData(Long testDataId, CalibrationTestData testData){
        CalibrationTestData updatedCalibrationTestData = dataRepository.findOne(testDataId);
        updatedCalibrationTestData.setGivenConsumption(testData.getGivenConsumption());
        updatedCalibrationTestData.setAcceptableError(testData.getAcceptableError());
        updatedCalibrationTestData.setVolumeOfStandard(testData.getVolumeOfStandard());
        updatedCalibrationTestData.setInitialValue(testData.getInitialValue());
        updatedCalibrationTestData.setEndValue(testData.getEndValue());
        updatedCalibrationTestData.setVolumeInDevice(testData.getVolumeInDevice());
        updatedCalibrationTestData.setTestTime(testData.getTestTime());
        updatedCalibrationTestData.setActualConsumption(testData.getActualConsumption());
        updatedCalibrationTestData.setConsumptionStatus(testData.getConsumptionStatus());
        updatedCalibrationTestData.setCalculationError(testData.getCalculationError());
        updatedCalibrationTestData.setTestResult(testData.getTestResult());
        updatedCalibrationTestData = dataRepository.save(updatedCalibrationTestData);
        return updatedCalibrationTestData;
    }
}