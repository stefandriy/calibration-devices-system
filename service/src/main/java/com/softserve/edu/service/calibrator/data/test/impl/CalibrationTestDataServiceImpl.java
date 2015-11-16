package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service that implements CRUD operations for test data.
 */
@Service
public class CalibrationTestDataServiceImpl implements CalibrationTestDataService {

    @Autowired
    private CalibrationTestDataRepository dataRepository;
    @Autowired
    private CalibrationTestRepository testRepository;

    @Override
    @Transactional
    public CalibrationTestData findTestData(Long id){
        return dataRepository.findOne(id);
    }

    @Override
    @Transactional
    public CalibrationTestData deleteTestData(Long id){
        CalibrationTestData deletedTestData = dataRepository.findOne(id);
        dataRepository.delete(id);
        return deletedTestData;
    }

    @Override
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
    @Override
    public CalibrationTestData createNewTestData(Long testId, DeviceTestData deviceTestData, int testDataId) {

        Double volumeInDevice = round(deviceTestData.getTestTerminalCounterValue(testDataId) - deviceTestData.getTestInitialCounterValue(testDataId), 2);
        Double actualConsumption = convertImpulsesPerSecToCubicMetersPerHour(
                deviceTestData.getTestCorrectedCurrentConsumption(testDataId),
                deviceTestData.getImpulsePricePerLitre());
        CalibrationTest calibrationTest = testRepository.findById(testId);
        CalibrationTestData сalibrationTestData = new CalibrationTestData(
                convertImpulsesPerSecToCubicMetersPerHour(deviceTestData.getTestSpecifiedConsumption(testDataId),
                        deviceTestData.getImpulsePricePerLitre()), //givenConsumption
                deviceTestData.getTestAllowableError(testDataId), //acceptableError
                deviceTestData.getTestSpecifiedImpulsesAmount(testDataId) * 1.0, //volumeOfStandard
                deviceTestData.getTestInitialCounterValue(testDataId), //initialValue
                deviceTestData.getTestTerminalCounterValue(testDataId), //endValue
                volumeInDevice,
                actualConsumption,
                countCalculationError(volumeInDevice, deviceTestData.getTestSpecifiedImpulsesAmount(testDataId) * 1.0), //calculationError
                calibrationTest);
        return сalibrationTestData;
    }

    private double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double convertImpulsesPerSecToCubicMetersPerHour(double impulses, long impLitPrice) {
        return round(3.6 * impulses / impLitPrice, 3);
    }

    private double countCalculationError(double counterVolume, double standardVolume) {
        if (standardVolume < 0.0001) {
            return 0.0;
        }
        double result = (counterVolume - standardVolume) / standardVolume * 100;
        return round(result, 2);
    }
}