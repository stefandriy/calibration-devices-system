package com.softserve.edu.dto;

import com.softserve.edu.entity.verification.Verification;

import java.util.Date;

/**
 * Created by Konyk on 29.07.2015.
 */
public class CalibrationTestPageItem {
    private Long id;
    private String name;
    private Date dateTest;
    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private String consumptionStatus;
    private Verification.CalibrationTestResult testResult;

    private String fullName;
    private String region;
    private String district;
    private String locality;
    private String street;

    private Long protocolId;
    private String protocolDate;
    private String protocolStatus;

    private String measurementDeviceId;
    private String measurementDeviceType;

    private String documentType;
    private String documentTypeName;
    private String documentDate;

    public CalibrationTestPageItem() {
    }

    public CalibrationTestPageItem(Long id, String name, Date dateTest, Integer temperature, Integer settingNumber,
                                   Double latitude, Double longitude, String consumptionStatus, Verification.CalibrationTestResult testResult) {
        this.id = id;
        this.name = name;
        this.dateTest = dateTest;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Verification.CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(Verification.CalibrationTestResult testResult) {
        this.testResult = testResult;
    }
}
