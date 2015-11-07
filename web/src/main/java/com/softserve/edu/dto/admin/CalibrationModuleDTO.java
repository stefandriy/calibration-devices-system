package com.softserve.edu.dto.admin;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by roman on 07.11.15.
 *
 */

@Setter
@Getter
public class CalibrationModuleDTO {

    private Long moduleId;
    private String deviceType;
    private String organizationCode;
    private String condDesignation;
    private String serialNumber;
    private String employeeFullName;
    private String telephone;
    private String moduleType;
    private String email;
    private String calibrationType;
    private String moduleNumber;
    private Boolean isActive;
    private String organization;
    private Date workDate;

    public CalibrationModuleDTO(Long id, String deviceType, String organizationCode,
                             String condDesignation, String serialNumber,
                             String employeeFullName, String telephone,
                             String moduleType, String email, String calibrationType,
                             String calibrator, Date workDate) {
        this.moduleId = id;
        this.deviceType = deviceType;
        this.organizationCode = organizationCode;
        this.condDesignation = condDesignation;
        this.serialNumber = serialNumber;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
        this.moduleType = moduleType;
        this.email = email;
        this.calibrationType = calibrationType;
        this.organization = calibrator;
        this.workDate = workDate;
    }


}
