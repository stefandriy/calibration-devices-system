package com.softserve.edu.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Konyk on 30.07.2015.
 */
@Entity
@Table(name = "CALIBRATION_TEST_IMG")
public class CalibrationTestIMG {
    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "calibrationTest_id")
    private CalibrationTest calibrationTest;

    private String imgName;

    @Temporal(TemporalType.DATE)
    private Date initialDate;

    public CalibrationTestIMG(CalibrationTest calibrationTest, String imgName, Date initialDate) {
        this.calibrationTest = calibrationTest;
        this.imgName = imgName;
        this.initialDate = initialDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public CalibrationTest getCalibrationTest() {
        return calibrationTest;
    }

    public void setCalibrationTest(CalibrationTest calibrationTest) {
        this.calibrationTest = calibrationTest;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }
}
