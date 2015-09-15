package com.softserve.edu.dto.calibrator;

import java.util.Date;
import java.util.Map;

/**
 * Created by Vasyl on 15.09.2015.
 */
public class CalibrationTaskDTO {

    private String place;

    private String counterStatus;

    private String counterNumber;

    private Map<String, Date> pickerDate;

    private String installationNumber;

    private String notes;

    public CalibrationTaskDTO() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(String counterStatus) {
        this.counterStatus = counterStatus;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public Map<String, Date> getPickerDate() {
        return pickerDate;
    }

    public void setPickerDate(Map<String, Date> pickerDate) {
        this.pickerDate = pickerDate;
    }

    public String getInstallationNumber() {
        return installationNumber;
    }

    public void setInstallationNumber(String installationNumber) {
        this.installationNumber = installationNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
