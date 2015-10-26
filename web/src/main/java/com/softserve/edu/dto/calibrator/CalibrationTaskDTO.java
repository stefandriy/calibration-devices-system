package com.softserve.edu.dto.calibrator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by Vasyl on 15.09.2015.
 */
public class CalibrationTaskDTO {

    private Date taskDate;

    private String serialNumber;

    private List<String> verificationsId;

    public CalibrationTaskDTO() {
    }

    public CalibrationTaskDTO(Date taskDate, String serialNumber, List<String> verificationsId) {
        this.taskDate = taskDate;
        this.serialNumber = serialNumber;
        this.verificationsId = verificationsId;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<String> getVerificationsId() {
        return verificationsId;
    }

    public void setVerificationsId(List<String> verificationsId) {
        this.verificationsId = verificationsId;
    }
}
