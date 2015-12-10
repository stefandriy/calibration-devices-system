package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.device.CalibrationModule;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CalibrationTaskDTO {

    private Long taskID;

    private String moduleNumber;

    private Date dateOfTask;

    private CalibrationModule.ModuleType moduleType;

    private String employeeFullName;

    private String telephone;

    private List<String> verificationsId;

    public CalibrationTaskDTO() {}

    public CalibrationTaskDTO(String moduleNumber, Date dateOfTask, List<String> verificationsId) {
        this.moduleNumber = moduleNumber;
        this.dateOfTask = dateOfTask;
        this.verificationsId = verificationsId;
    }

    public CalibrationTaskDTO(Long taskID, String moduleNumber, Date dateOfTask,
                              CalibrationModule.ModuleType moduleType, String employeeFullName, String telephone) {
        this.taskID = taskID;
        this.moduleNumber = moduleNumber;
        this.dateOfTask = dateOfTask;
        this.moduleType = moduleType;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
    }
}
