package com.softserve.edu.entity.verification.calibration;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calibrationTestDataId")
    private CalibrationTestData calibrationTestData;

    private String imgName;

    @Temporal(TemporalType.DATE)
    private Date initialDate;

    public CalibrationTestIMG(CalibrationTestData calibrationTestData, String imgName) {
        this.calibrationTestData = calibrationTestData;
        this.imgName = imgName;
        this.initialDate = new Date();
    }
}
