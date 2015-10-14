package com.softserve.edu.dto;



public class CalibrationTestSearch {

    private String id;
    private String name;
    private String dateTest;
    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private String consumptionStatus;

    private String client_full_name;
    private String street;
    private String region;
    private String district;
    private String locality;

    private String status;
    private Long protocol_id;
    private String protocol_status;

    private Long measurement_device_id;
    private String measurement_device_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTest() {
        return dateTest;
    }

    public void setDateTest(String dateTest) {
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

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getClient_full_name() {
        return client_full_name;
    }

    public void setClient_full_name(String client_full_name) {
        this.client_full_name = client_full_name;
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

    public Long getProtocol_id() {
        return protocol_id;
    }

    public void setProtocol_id(Long protocol_id) {
        this.protocol_id = protocol_id;
    }

    public String getProtocol_status() {
        return protocol_status;
    }

    public void setProtocol_status(String protocol_status) {
        this.protocol_status = protocol_status;
    }
}
