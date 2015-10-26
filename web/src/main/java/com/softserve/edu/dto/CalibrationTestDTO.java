package com.softserve.edu.dto;

import java.util.Date;

import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.verification.ConsumptionStatus;
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
    private ConsumptionStatus consumptionStatus;
    private CalibrationTestResult testResult;

    private String clientFullName;
    private String street;
    private String region;
    private String district;
    private String locality;

    private Long protocolId;

    private Long measurementDeviceId;
    private String measurementDeviceType;


    private DocumentType documentType;
    private String documentTypeName;
    private String documentDate;

    public CalibrationTestDTO(String s, Date dateTest, Integer temperature, Integer settingNumber, Double latitude, Double longitude, String consumptionStatus, CalibrationTestResult testResult, String fullName, String street, String region, String district, String locality, Long calibrationTestId, Long id, Device.DeviceType deviceType) {
    }

    public CalibrationTestDTO(String name, Integer temperature, Integer settingNumber, Double latitude,
                              Double longitude, ConsumptionStatus consumptionStatus, CalibrationTestResult testResult) {
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

    public CalibrationTestDTO(String id, String name, Date dateTest, Integer temperature, Integer settingNumber, Double latitude, Double longitude, ConsumptionStatus consumptionStatus, CalibrationTestResult testResult, String clientFullName, String street, String region, String district, String locality, Long protocolId, Long  measurementDeviceId, String measurementDeviceType, String documentTypeName/*, String documentDate*/) {
        this.id = id;
        this.name = name;
        this.dateTest = dateTest;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
        this.clientFullName = clientFullName;
        this.street = street;
        this.region = region;
        this.district = district;
        this.locality = locality;
        this.protocolId = protocolId;
        this. measurementDeviceId =  measurementDeviceId;
        this.measurementDeviceType = measurementDeviceType;
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

    public ConsumptionStatus getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(ConsumptionStatus consumptionStatus) {
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

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
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

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getMeasurementDeviceId() {
        return  measurementDeviceId;
    }

    public void setMeasurementDeviceId(Long  measurementDeviceId) {
        this. measurementDeviceId =  measurementDeviceId;
    }

    public String getMeasurementDeviceType() {
        return measurementDeviceType;
    }

    public void setMeasurementDeviceType(String measurementDeviceType) {
        this.measurementDeviceType = measurementDeviceType;
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