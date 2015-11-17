package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Calibration Test entity.
 * Contains data about measurement device calibration test.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "CALIBRATION_TEST")
@NoArgsConstructor
public class CalibrationTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTest;

    private Integer temperature;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Verification.ConsumptionStatus consumptionStatus;

    @Enumerated(EnumType.STRING)
    private Verification.CalibrationTestResult testResult;

    private String photoPath;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name",
                    column = @Column(name = "documentName")),
            @AttributeOverride(name = "sign",
                    column = @Column(name = "documentSign"))
    })
    private MeteorologicalDocument meteorologicalDocument;

    @ManyToOne
    @JoinColumn(name = "verificationId")
    private Verification verification;

    @OneToMany(mappedBy = "calibrationTest", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @OrderColumn(name = "testPosition")
    private List<CalibrationTestData> calibrationTestDataList;

    public CalibrationTest(String name, Integer temperature, Integer settingNumber, Double latitude,
                             Double longitude, Verification.ConsumptionStatus consumptionStatus, Verification.CalibrationTestResult testResult) {
        this.name = name;
        this.dateTest = new Date();
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.consumptionStatus = consumptionStatus;
        this.testResult = testResult;
    }
    public CalibrationTest(String name, Integer temperature, Integer settingNumber, Double latitude,
                           Double longitude, Long unixTime) {
        this.name = name;
        this.dateTest = new Date(unixTime);
        this.temperature = temperature;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    public CalibrationTest(CalibrationTest calibrationTest, Verification verification) {
        this.verification = verification;
    }
}
