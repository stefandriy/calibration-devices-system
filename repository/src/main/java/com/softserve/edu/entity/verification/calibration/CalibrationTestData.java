package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * Calibration Test Data entity.
 * Contains information about measurement device calibration test data.
 */
@Entity
@Table(name = "CALIBRATION_TEST_DATA")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class CalibrationTestData {

    @Id
    @GeneratedValue
    private Long id;

    private Double givenConsumption;
    private Long acceptableError;
    private Double volumeOfStandard;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;

    @Temporal(TemporalType.TIMESTAMP)

    private Date testTime;
    private Double actualConsumption;
    private Verification.ConsumptionStatus consumptionStatus;
    private Double calculationError;
    private Verification.CalibrationTestResult testResult;

    @ManyToOne
    @JoinColumn(name = "calibrationTestId")
    private CalibrationTest calibrationTest;

    /*public CalibrationTestData(
            Double givenConsumption, Long acceptableError, Double volumeOfStandard, Double initialValue,
            Double endValue, Double volumeInDevice, Double actualConsumption, Verification.ConsumptionStatus consumptionStatus,
            Double calculationError, Verification.CalibrationTestResult testResult, CalibrationTest calibrationTest
    ) {
        this.givenConsumption = givenConsumption;
        this.acceptableError = acceptableError;
        this.volumeOfStandard = volumeOfStandard;
        this.initialValue = initialValue;
        this.endValue = endValue;
        this.volumeInDevice = volumeInDevice;
        this.testTime = new Date();
        this.actualConsumption = actualConsumption;
        this.consumptionStatus = consumptionStatus;
        this.calculationError = calculationError;
        this.testResult = testResult;
        this.calibrationTest = calibrationTest;
    }*/

    public CalibrationTestData(
            Double givenConsumption, Long acceptableError, Double volumeOfStandard, Double initialValue,
            Double endValue, Double volumeInDevice, Double actualConsumption,
            Double calculationError, CalibrationTest calibrationTest)
    {
        this.givenConsumption = givenConsumption;
        this.acceptableError = acceptableError;
        this.volumeOfStandard = volumeOfStandard;
        this.initialValue = initialValue;
        this.endValue = endValue;
        this.volumeInDevice = volumeInDevice;
        this.testTime = new Date();
        this.actualConsumption = actualConsumption;
        this.calculationError = calculationError;
        if (this.getActualConsumption() <= this.getAcceptableError()) {
            this.testResult = Verification.CalibrationTestResult.SUCCESS;
        } else {
            this.testResult = Verification.CalibrationTestResult.FAILED;
        }
       /* if((this.getGivenConsumption() + (this.getGivenConsumption()*this.getEndValue()/100)<= this.getActualConsumption())
        & (this.getActualConsumption()<= (this.getGivenConsumption() - (this.getGivenConsumption()*this.getInitialValue())/100))){
        this.consumptionStatus=Verification.ConsumptionStatus.IN_THE_AREA;
        }else{
        this.consumptionStatus=Verification.ConsumptionStatus.NOT_IN_THE_AREA;
        }*/

        this.calibrationTest = calibrationTest;
    }
}
