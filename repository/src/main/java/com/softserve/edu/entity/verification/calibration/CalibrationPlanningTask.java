package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PLANNING_TEST")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class CalibrationPlanningTask {

    @Id
    @GeneratedValue
    private Long id;

    private String placeOfCalibration;
    private String removeStatus;
    private String serialNumberOfMeasuringInstallation;

    @Temporal(TemporalType.DATE)
    private Date dateOfVisit;

    @Temporal(TemporalType.DATE)
    private Date dateOfVisitTo;

    private int floor;
    private String serialNumberOfCounter;
    private String notes;

    @OneToOne
    private Verification verification;


    public CalibrationPlanningTask(
            String placeOfCalibration, String removeStatus, String serialNumberOfMeasuringInstallation, Date dateOfVisit,
            Date dateOfVisitTo, int floor, String serialNumberOfCounter, String notes
    ) {
        this.placeOfCalibration = placeOfCalibration;
        this.removeStatus = removeStatus;
        this.serialNumberOfMeasuringInstallation = serialNumberOfMeasuringInstallation;
        this.dateOfVisit = dateOfVisit;
        this.dateOfVisitTo = dateOfVisitTo;
        this.floor = floor;
        this.serialNumberOfCounter = serialNumberOfCounter;
        this.notes = notes;
    }
}
