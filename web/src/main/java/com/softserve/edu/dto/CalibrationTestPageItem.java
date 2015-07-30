package com.softserve.edu.dto;

/**
 * Created by Konyk on 29.07.2015.
 */
public class CalibrationTestPageItem {
    private Long id;
    private String name;
    private String dateTest;
    private String temperature;
    private String settingNumber;
    private String latitude;
    private String longitude;
    private String consumptionStatus;
    private String photoPath;

    public CalibrationTestPageItem() {
    }

    public CalibrationTestPageItem(Long id, String name, String dateTest, String temperature, String settingNumber,
                                   String latitude, String longitude, String consumptionStatus, String photoPath) {
        this.id = id;
        this.name = name;
        this.dateTest = dateTest;
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.photoPath = photoPath;
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

    public String getDateTest() {
        return dateTest;
    }

    public void setDateTest(String dateTest) {
        this.dateTest = dateTest;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSettingNumber() {
        return settingNumber;
    }

    public void setSettingNumber(String settingNumber) {
        this.settingNumber = settingNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
