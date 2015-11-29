package com.softserve.edu.dto.calibrator;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CalibrationTaskDTO {

    private String moduleNumber;

    private Date taskDate;

    private List<String> verificationsId;

    public CalibrationTaskDTO() {
    }

    public CalibrationTaskDTO(String moduleNumber, Date taskDate, List<String> verificationsId) {
        this.moduleNumber = moduleNumber;
        this.taskDate = taskDate;
        this.verificationsId = verificationsId;
    }


}
