package com.softserve.edu.dto;

import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;


public class CalibrationTestDataDTO {
    private Boolean dataAvailable;
    private String testNumber;
    private Double givenConsumption;
    private Long acceptableError;
    private Long volumeOfStandart;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;
    private Double testTime;
    private Double actualConsumption;
    private String consumptionStatus;
    private Double calculationError;
    private String beginPhoto;
    private String endPhoto;
    private CalibrationTestResult testResult;

    public Boolean getDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(Boolean dataAvailable) {
        this.dataAvailable = dataAvailable;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public Double getGivenConsumption() {
        return givenConsumption;
    }

    public void setGivenConsumption(Double givenConsumption) {
        this.givenConsumption = givenConsumption;
    }

    public Long getAcceptableError() {
        return acceptableError;
    }

    public void setAcceptableError(Long acceptableError) {
        this.acceptableError = acceptableError;
    }

    public Long getVolumeOfStandart() {
        return volumeOfStandart;
    }

    public void setVolumeOfStandart(Long volumeOfStandart) {
        this.volumeOfStandart = volumeOfStandart;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getEndValue() {
        return endValue;
    }

    public void setEndValue(Double endValue) {
        this.endValue = endValue;
    }

    public Double getVolumeInDevice() {
        return volumeInDevice;
    }

    public void setVolumeInDevice(Double volumeInDevice) {
        this.volumeInDevice = volumeInDevice;
    }

    public Double getTestTime() {
        return testTime;
    }

    public void setTestTime(Double testTime) {
        this.testTime = testTime;
    }

    public Double getActualConsumption() {
        return actualConsumption;
    }

    public void setActualConsumption(Double actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public Double getCalculationError() {
        return calculationError;
    }

    public void setCalculationError(Double calculationError) {
        this.calculationError = calculationError;
    }

    public String getBeginPhoto() {
        return beginPhoto;
    }

    public void setBeginPhoto(String beginPhoto) {
        this.beginPhoto = beginPhoto;
    }

    public String getEndPhoto() {
        return endPhoto;
    }

    public void setEndPhoto(String endPhoto) {
        this.endPhoto = endPhoto;
    }

    public CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(CalibrationTestResult testResult) {
        this.testResult = testResult;
    }

    public CalibrationTestData saveTestData() {
        CalibrationTestData calibrationTestData = new CalibrationTestData();
        calibrationTestData.setGivenConsumption(givenConsumption);
        calibrationTestData.setAcceptableError(acceptableError);
        calibrationTestData.setVolumeOfStandard(volumeOfStandart);
        calibrationTestData.setInitialValue(initialValue);
        calibrationTestData.setEndValue(endValue);
        calibrationTestData.setVolumeInDevice(volumeInDevice);
        calibrationTestData.setActualConsumption(actualConsumption);
        calibrationTestData.setConsumptionStatus(consumptionStatus);
        calibrationTestData.setCalculationError(calculationError);
        calibrationTestData.setTestResult(CalibrationTestResult.SUCCESS);
        return calibrationTestData;
    }
}