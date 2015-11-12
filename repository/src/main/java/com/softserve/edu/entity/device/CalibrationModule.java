package com.softserve.edu.entity.device;

import com.softserve.edu.entity.organization.Organization;

import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.*;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "CALIBRATION_MODULE")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CalibrationModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Enumerated(EnumType.STRING)
    private Device.DeviceType deviceType;

    private String organizationCode;

    private String condDesignation;

    private String serialNumber;

    private String employeeFullName;

    private String telephone;

    private String moduleType;

    private String email;

    private String calibrationType;

    private String moduleNumber;//generates

    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    @Temporal(TemporalType.DATE)
    private Date workDate;

    {
        if (moduleId != null && moduleNumber == null) {
            generateSerialNumber();
        }
    }

    /* @OneToMany(mappedBy = "module")
    private Set<CalibrationTask> tasks; */


    public CalibrationModule(Device.DeviceType deviceType, String organizationCode,
                             String condDesignation, String serialNumber,
                             String employeeFullName, String telephone,
                             String moduleType, String email, String calibrationType,
                             Organization organization, Date workDate) {
        this.deviceType = deviceType;
        this.organizationCode = organizationCode;
        this.condDesignation = condDesignation;
        this.serialNumber = serialNumber;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
        this.moduleType = moduleType;
        this.email = email;
        this.calibrationType = calibrationType;
        this.organization = organization;
        this.workDate = workDate;
    }

    public void updateFields(CalibrationModule calibrationModule) {
        this.deviceType = calibrationModule.getDeviceType();
        this.organizationCode = calibrationModule.getOrganizationCode();
        this.condDesignation = calibrationModule.getCondDesignation();
        this.serialNumber = calibrationModule.getSerialNumber();
        this.employeeFullName = calibrationModule.getEmployeeFullName();
        this.telephone = calibrationModule.getTelephone();
        this.moduleType = calibrationModule.getModuleType();
        this.email = calibrationModule.getEmail();
        this.calibrationType = calibrationModule.getCalibrationType();
        this.organization = calibrationModule.getOrganization();
        this.workDate = calibrationModule.getWorkDate();
    }

    private void generateSerialNumber() {
        StringBuilder sb = new StringBuilder();
        switch (deviceType) {
            case WATER: sb.append("1"); break;
            case GASEOUS: sb.append("2"); break;
            case ELECTRICAL: sb.append("3"); break;
            case THERMAL: sb.append("4"); break;
            default: break;
        }
        sb.append(String.format("%03d", String.valueOf(moduleId)));
    }

}
