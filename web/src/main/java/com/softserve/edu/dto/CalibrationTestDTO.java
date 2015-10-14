package com.softserve.edu.dto;

import java.util.Date;

import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;


public class CalibrationTestDTO  {

    private String id;
    private String name;
    private Date  dateTest;
    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private String consumptionStatus;
    private CalibrationTestResult testResult;

    private String client_full_name;
    private String street;
    private String region;
    private String district;
    private String locality;

    private Long protocol_id;

    private Long measurement_device_id;
    private String measurement_device_type;


    private DocumentType documentType;
    private String documentTypeName;
    private String documentDate;

    public CalibrationTestDTO(String s, Date dateTest, Integer temperature, Integer settingNumber, Double latitude, Double longitude, String consumptionStatus, CalibrationTestResult testResult, String fullName, String street, String region, String district, String locality, Long calibrationTestId, Long id, DeviceType deviceType) {
    }

    public CalibrationTestDTO(String name, Integer temperature, Integer settingNumber, Double latitude,
                              Double longitude, String consumptionStatus, CalibrationTestResult testResult) {
        this.name = name;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
    }

    public CalibrationTestDTO(CalibrationTest calibrationTest) {
        super();
        this.name = calibrationTest.getName();
        this.dateTest = calibrationTest.getDateTest();
        this.temperature = calibrationTest.getTemperature();
        this.settingNumber = calibrationTest.getSettingNumber();
        this.latitude = calibrationTest.getLatitude();
        this.longitude = calibrationTest.getLongitude();
        this.consumptionStatus = calibrationTest.getConsumptionStatus();
    }

    public CalibrationTestDTO(String id, String name, Date dateTest, Integer temperature, Integer settingNumber, Double latitude, Double longitude, String consumptionStatus, CalibrationTestResult testResult, String client_full_name, String street, String region, String district, String locality, Long protocol_id, Long measurement_device_id, String measurement_device_type, String documentTypeName/*, String documentDate*/) {
        this.id = id;
        this.name = name;
        this.dateTest = dateTest;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
        this.client_full_name = client_full_name;
        this.street = street;
        this.region = region;
        this.district = district;
        this.locality = locality;
        this.protocol_id = protocol_id;
        this.measurement_device_id = measurement_device_id;
        this.measurement_device_type = measurement_device_type;
        this.documentTypeName = documentTypeName;
    //    this.documentDate = documentDate;
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
        this.dateTest = new Date();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_full_name() {
        return client_full_name;
    }

    public void setClient_full_name(String client_full_name) {
        this.client_full_name = client_full_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Long getProtocol_id() {
        return protocol_id;
    }

    public void setProtocol_id(Long protocol_id) {
        this.protocol_id = protocol_id;
    }

    public Long getMeasurement_device_id() {
        return measurement_device_id;
    }

    public void setMeasurement_device_id(Long measurement_device_id) {
        this.measurement_device_id = measurement_device_id;
    }

    public String getMeasurement_device_type() {
        return measurement_device_type;
    }

    public void setMeasurement_device_type(String measurement_device_type) {
        this.measurement_device_type = measurement_device_type;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public CalibrationTest saveCalibrationTest() {
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setName(name);
        calibrationTest.setDateTest(new Date());
        calibrationTest.setTemperature(temperature);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(consumptionStatus);
        calibrationTest.setTestResult(CalibrationTestResult.SUCCESS);
        return calibrationTest;
    }
}