package com.softserve.edu.entity.device;

import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.organization.Organization;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Vasyl on 08.10.2015.
 */
@Entity
@Table(name = "CALIBRATION_MODULE")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class CalibrationModule {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private String organizationCode;

    private String condDesignation;

    private String serialNumber;

    private String employeeFullName;

    private String telephone;

    private String moduleType;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    private Date workDate;

    public CalibrationModule(String id, DeviceType deviceType, String organizationCode, String condDesignation, String serialNumber,
                             String employeeFullName, String telephone, String moduleType, String email, Organization calibrator, Date workDate){
        super();
        this.id = id;
        this.deviceType = deviceType;
        this.organizationCode = organizationCode;
        this.condDesignation = condDesignation;
        this.serialNumber = serialNumber;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
        this.moduleType = moduleType;
        this.email = email;
        this.organization = calibrator;
        this.workDate = workDate;
    }

}
