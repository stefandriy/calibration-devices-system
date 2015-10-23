package com.softserve.edu.entity.device;

import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Vasyl on 08.10.2015.
 */
@Entity
@Table(name = "CALIBRATION_MODULE")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CalibrationModule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long moduleId;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private String organizationCode;

    private String condDesignation;

    private String serialNumber;

    private String employeeFullName;

    private String telephone;

    private String moduleType;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    @Temporal(TemporalType.DATE)
    private Date workDate;

    @OneToMany(mappedBy = "module")
    private Set<CalibrationTask> tasks;


    public CalibrationModule(Long id, DeviceType deviceType, String organizationCode, String condDesignation, String serialNumber,
                             String employeeFullName, String telephone, String moduleType, String email, Organization calibrator, Date workDate){
        super();
        this.moduleId = id;
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
