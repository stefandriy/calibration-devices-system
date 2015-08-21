package com.softserve.edu.dto.calibrator;

import com.softserve.edu.dto.CalibrationTestDataDTO;
import com.softserve.edu.entity.util.CalibrationTestResult;

import java.util.Date;
import java.util.List;

/**
 * Created by Konyk on 21.08.2015.
 */
public class GlobalCalibrationTestDTO {
    private String name;
    private Date dateTest;
    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private String consumptionStatus;
    private CalibrationTestResult testResult;

    private List<CalibrationTestDataDTO> listTestData;


    public GlobalCalibrationTestDTO(){};

    public GlobalCalibrationTestDTO(String name, Date dateTest, Integer temperature, Integer settingNumber, Double latitude, Double longitude, String consumptionStatus,
                                    CalibrationTestResult testResult, List<CalibrationTestDataDTO> listTestData) {
        this.name = name;
        this.dateTest = dateTest;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
        this.listTestData = listTestData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateTest() {
        return dateTest;
    }

    public void setDateTest(Date dateTest) {
        this.dateTest = dateTest;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getSettingNumber() {
        return settingNumber;
    }

    public void setSettingNumber(Integer settingNumber) {
        this.settingNumber = settingNumber;
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

    public List<CalibrationTestDataDTO> getListTestData() {
        return listTestData;
    }

    public void setListTestData(List<CalibrationTestDataDTO> listTestData) {
        this.listTestData = listTestData;
    }
}
