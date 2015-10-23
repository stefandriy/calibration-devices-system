package com.softserve.edu.entity.device;

import com.softserve.edu.entity.organization.Organization;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CALIBRATION_MODULE")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class CalibrationModule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Device.DeviceType deviceType;

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


    public CalibrationModule(Long id, Device.DeviceType deviceType, String organizationCode, String condDesignation, String serialNumber,
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
