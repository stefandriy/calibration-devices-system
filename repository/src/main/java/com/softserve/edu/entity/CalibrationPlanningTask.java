package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Vasyl on 09.09.2015.
 */

@Entity
@Table(name = "`PlanningTest`")
public class CalibrationPlanningTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String placeOfcalibration;

    @Column
    private String removeStatus;

    @Column
    private String serialNumberOfMeasuringInstallation;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dateOfVisit;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dateOfVisitTo;

    @Column
    private int floor;

    @Column
    private String serialNumberOfCounter;

    @Column
    private String notes;

    @OneToOne
    private Verification verification;

    public CalibrationPlanningTask() {

    }


    public CalibrationPlanningTask(String placeOfcalibration, String removeStatus,
                                   String serialNumberOfMeasuringInstallation, Date dateOfVisit,
                                   Date dateOfVisitTo, int floor, String serialNumberOfCounter,
                                   String notes) {
        this.placeOfcalibration = placeOfcalibration;
        this.removeStatus = removeStatus;
        this.serialNumberOfMeasuringInstallation = serialNumberOfMeasuringInstallation;
        this.dateOfVisit = dateOfVisit;
        this.dateOfVisitTo = dateOfVisitTo;
        this.floor = floor;
        this.serialNumberOfCounter = serialNumberOfCounter;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public String getPlaceOfcalibration() {
        return placeOfcalibration;
    }

    public void setPlaceOfcalibration(String placeOfcalibration) {
        this.placeOfcalibration = placeOfcalibration;
    }

    public String getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(String removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getSerialNumberOfMeasuringInstallation() {
        return serialNumberOfMeasuringInstallation;
    }

    public void setSerialNumberOfMeasuringInstallation(String serialNumberOfMeasuringInstallation) {
        this.serialNumberOfMeasuringInstallation = serialNumberOfMeasuringInstallation;
    }

    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public Date getDateOfVisitTo() {
        return dateOfVisitTo;
    }

    public void setDateOfVisitTo(Date dateOfVisitTo) {
        this.dateOfVisitTo = dateOfVisitTo;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getSerialNumberOfCounter() {
        return serialNumberOfCounter;
    }

    public void setSerialNumberOfCounter(String serialNumberOfCounter) {
        this.serialNumberOfCounter = serialNumberOfCounter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("placeOfcalibration", placeOfcalibration)
                .append("removeStatus", removeStatus)
                .append("serialNumberOfMeasuringInstallation", serialNumberOfMeasuringInstallation)
                .append("dateOfVisit", dateOfVisit)
                .append("dateOfVisitTo", dateOfVisitTo)
                .append("floor", floor)
                .append("serialNumberOfCounter", serialNumberOfCounter)
                .append("notes", notes)
                .append("verification", verification)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(placeOfcalibration)
                .append(removeStatus)
                .append(serialNumberOfMeasuringInstallation)
                .append(dateOfVisit)
                .append(dateOfVisitTo)
                .append(floor)
                .append(serialNumberOfCounter)
                .append(notes)
                .append(verification)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CalibrationPlanningTask) {
            final CalibrationPlanningTask other = (CalibrationPlanningTask) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(placeOfcalibration, other.getPlaceOfcalibration())
                    .append(removeStatus, other.getRemoveStatus())
                    .append(serialNumberOfMeasuringInstallation, other.getSerialNumberOfMeasuringInstallation())
                    .append(dateOfVisit, other.getDateOfVisit())
                    .append(dateOfVisitTo, other.getDateOfVisitTo())
                    .append(floor, other.getFloor())
                    .append(serialNumberOfCounter, other.getSerialNumberOfCounter())
                    .append(notes, other.getNotes())
                    .append(verification, other.getVerification())
                    .isEquals();
        } else {
            return false;
        }
    }

}
