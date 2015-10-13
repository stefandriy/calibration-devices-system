package com.softserve.edu.dto;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CalibrationTestFileDataDTO {
    private String fileName;

    private String counterNumber;

    private Date testDate;

    private Integer temperature;

    private Long accumulatedVolume;

    private Long installmentNumber;

    private Double latitude;

    private Double longitude;

    private String consumptionStatus;

    private String testPhoto;

    private CalibrationTestResult testResult;

    private List<CalibrationTestDataDTO> listTestData;

    public CalibrationTestFileDataDTO() {}

    public CalibrationTestFileDataDTO(DeviceTestData testData) {
        this.fileName = testData.getFileName();
        this.counterNumber = testData.getCurrentCounterNumber();
        this.testDate = new Date(testData.getUnixTime());
        this.temperature = testData.getTemperature();
        //this.accumulatedVolume = testData.getInitialCapacity(); // not sure if this is exactly what needed
        this.installmentNumber = testData.getInstallmentNumber();
        this.latitude = testData.getLatitude();
        this.longitude = testData.getLongitude();
        this.testPhoto = testData.getTestPhoto();

        this.listTestData = new ArrayList<>();

        for (int i = 1; i <= 6; ++i ) {
            CalibrationTestDataDTO testDataDTO = new CalibrationTestDataDTO();
            int testNumber = testData.getTestNumber(i);
            if (testNumber == 0) {
                testDataDTO.setDataAvailable(false);
                continue;
            } else {
                testDataDTO.setDataAvailable(true);
            }
            String testNumberStr = "Test " + testNumber / 10;
            Integer testRepeat = testNumber % 10;
            testNumberStr += ((testRepeat != 0) ? " Repeat " + testRepeat : "");
            testDataDTO.setTestNumber(testNumberStr);
            testDataDTO.setGivenConsumption(convertImpulsesPerSecToCubicMetersPerHour(testData.getTestSpecifiedConsumption(i) * 1.0,
                    testData.getImpulsePricePerLitre()));
            testDataDTO.setAcceptableError(testData.getTestAllowableError(i));
            testDataDTO.setInitialValue(testData.getTestInitialCounterValue(i));
            testDataDTO.setEndValue(testData.getTestTerminalCounterValue(i));
            testDataDTO.setVolumeInDevice(round(testDataDTO.getEndValue() - testDataDTO.getInitialValue(), 2));
            testDataDTO.setTestTime(round(testData.getTestDuration(i), 1));
            testDataDTO.setVolumeOfStandart(testData.getTestSpecifiedImpulsesAmount(i));
            testDataDTO.setCalculationError(countCalculationError(testDataDTO.getVolumeInDevice(), (double)testDataDTO.getVolumeOfStandart()));
            testDataDTO.setBeginPhoto(testData.getBeginPhoto(i));
            testDataDTO.setEndPhoto(testData.getEndPhoto(i));

            this.listTestData.add(testDataDTO);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Long getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public void setAccumulatedVolume(Long accumulatedVolume) {
        this.accumulatedVolume = accumulatedVolume;
    }

    public Long getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Long installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(CalibrationTestResult testResult) {
        this.testResult = testResult;
    }

    public String getTestPhoto() {
        return testPhoto;
    }

    public void setTestPhoto(String testPhoto) {
        this.testPhoto = testPhoto;
    }

    public List<CalibrationTestDataDTO> getListTestData() {
        return listTestData;
    }

    public void setListTestData(List<CalibrationTestDataDTO> listTestData) {
        this.listTestData = listTestData;
    }

    private Double round(Double val, int scale) {
        return new BigDecimal(val.toString()).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private Double convertImpulsesPerSecToCubicMetersPerHour(Double impulses, Long impLitPrice) {
        return round(3.6 * impulses / impLitPrice, 3);
    }

    private Double countCalculationError(Double counterVolume, Double standardVolume) {
        if (standardVolume < 0.0001) {
            return 0.0;
        }
        Double result = (counterVolume - standardVolume) / standardVolume * 100;
        System.out.println(result);
        return round(result, 2);
    }
}
