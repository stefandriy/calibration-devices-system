package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;


/**
 * Calibration Test Data entity.
 * Contains information about measurement device calibration test data.
 */
@Entity
@Table(name = "`CALIBRATION_TEST_DATA`")
public class CalibrationTestData {
    @Id
    @GeneratedValue
    private Long id;
    private Double givenConsumption;
    private Integer acceptableError;
    private Integer volumeOfStandard;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;
    private Double testTime;
    private Double actualConsumption;
    private String consumptionStatus;
    private Double calculationError;
    private String testResult;

    @ManyToOne
    @JoinColumn(name = "calibrationTest_id")
    private CalibrationTest calibrationTest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGivenConsumption() {
        return givenConsumption;
    }

    public void setGivenConsumption(Double givenConsumption) {
        this.givenConsumption = givenConsumption;
    }

    public Integer getAcceptableError() {
        return acceptableError;
    }

    public void setAcceptableError(Integer acceptableError) {
        this.acceptableError = acceptableError;
    }

    public Integer getVolumeOfStandard() {
        return volumeOfStandard;
    }

    public void setVolumeOfStandard(Integer volumeOfStandard) {
        this.volumeOfStandard = volumeOfStandard;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getEndValue() {
        return endValue;
    }

    public void setEndValue(Double endValue) {
        this.endValue = endValue;
    }

    public Double getVolumeInDevice() {
        return volumeInDevice;
    }

    public void setVolumeInDevice(Double volumeInDevice) {
        this.volumeInDevice = volumeInDevice;
    }

    public Double getTestTime() {
        return testTime;
    }

    public void setTestTime(Double testTime) {
        this.testTime = testTime;
    }

    public Double getActualConsumption() {
        return actualConsumption;
    }

    public void setActualConsumption(Double actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public Double getCalculationError() {
        return calculationError;
    }

    public void setCalculationError(Double calculationError) {
        this.calculationError = calculationError;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public CalibrationTest getCalibrationTest() {
        return calibrationTest;
    }

    public void setCalibrationTest(CalibrationTest calibrationTest) {
        this.calibrationTest = calibrationTest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("givenConsumption", givenConsumption)
                .append("acceptableError", acceptableError)
                .append("volumeOfStandard", volumeOfStandard)
                .append("initialValue", initialValue)
                .append("endValue", endValue)
                .append("volumeInDevice", volumeInDevice)
                .append("testTime", testTime)
                .append("actualConsumption", actualConsumption)
                .append("consumptionStatus", consumptionStatus)
                .append("calculationError", calculationError)
                .append("testResult", testResult)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(id)
                .append(givenConsumption)
                .append(acceptableError)
                .append(volumeOfStandard)
                .append(initialValue)
                .append(endValue)
                .append(volumeInDevice)
                .append(testTime)
                .append(actualConsumption)
                .append(consumptionStatus)
                .append(calculationError)
                .append(testResult)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof CalibrationTestData){
            final CalibrationTestData other = (CalibrationTestData) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(givenConsumption, other.givenConsumption)
                    .append(acceptableError, other.acceptableError)
                    .append(volumeOfStandard, other.volumeOfStandard)
                    .append(initialValue, other.initialValue)
                    .append(endValue, other.endValue)
                    .append(volumeInDevice, other.volumeInDevice)
                    .append(testTime, other.testTime)
                    .append(actualConsumption, other.actualConsumption)
                    .append(consumptionStatus, other.consumptionStatus)
                    .append(calculationError, other.calculationError)
                    .append(testResult, other.testResult)
                    .isEquals();
        } else{
            return false;
        }
    }
}
