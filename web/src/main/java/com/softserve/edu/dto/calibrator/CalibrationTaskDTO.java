package com.softserve.edu.dto.calibrator;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CalibrationTaskDTO {

    private String serialNumber;

    private Date taskDate;

    private List<String> verificationsId;

    public CalibrationTaskDTO() {
    }

    public CalibrationTaskDTO(String serialNumber, Date taskDate, List<String> verificationsId) {
        this.serialNumber = serialNumber;
        this.taskDate = taskDate;
        this.verificationsId = verificationsId;
    }


}
