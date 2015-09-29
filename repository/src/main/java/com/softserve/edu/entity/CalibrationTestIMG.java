package com.softserve.edu.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "CALIBRATION_TEST_IMG")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalibrationTestIMG {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calibrationTestId")
    private CalibrationTest calibrationTest;

    private String imgName;

    @Temporal(TemporalType.DATE)
    private Date initialDate;

    public CalibrationTestIMG(CalibrationTest calibrationTest, String imgName) {
        this.calibrationTest = calibrationTest;
        this.imgName = imgName;
        this.initialDate = new Date();
    }
}
