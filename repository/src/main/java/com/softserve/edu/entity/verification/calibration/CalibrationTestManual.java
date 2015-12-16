package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.device.CalibrationModule;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by Misha on 12/11/2015.
 * Calibration Test  Manual entity.
 * Contains data about manual calibration test
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "CALIBRATION_TEST_MANUAL")
@NoArgsConstructor
public class CalibrationTestManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String pathToScan;
    private Integer numberOfTest;
    private Long generateNumberTest;


    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTest;

    @ManyToOne
    @JoinColumn(name = "calibrationModuleId")
    private CalibrationModule calibrationModule;

    @OneToMany(mappedBy = "calibrationTestManual", cascade = CascadeType.ALL)
    private List<CalibrationTestDataManual> calibrationTestDataManual;

    public CalibrationTestManual(String pathToScan, Integer numberOfTest, Long generateNumberTest, Date dateTest, CalibrationModule calibrationModule) {
        this.pathToScan = pathToScan;
        this.numberOfTest = numberOfTest;
        this.generateNumberTest = generateNumberTest;
        this.dateTest = dateTest;
        this.calibrationModule = calibrationModule;
    }


}
