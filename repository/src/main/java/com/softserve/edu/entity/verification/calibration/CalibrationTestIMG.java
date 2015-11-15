package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.calibration.CalibrationTest;
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
    @JoinColumn(name = "calibrationTestDataId")
    private CalibrationTestData calibrationTestData;

    private String imgName;

    @Temporal(TemporalType.DATE)
    private Date initialDate;

    public CalibrationTestIMG(CalibrationTest calibrationTest, String imgName) {
//        this.calibrationTest = calibrationTest;
        this.imgName = imgName;
        this.initialDate = new Date();
    }
}
