package com.softserve.edu.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Date;

/**
 * Created by Misha on 12/13/2015.
 */
@Getter
@Setter
public class CalibrationTestManualDTO {

    private String  serialNumber;
    private List<CalibrationTestDataManualDTO>listOfCalibrationTestDataManual;
    private Integer numberOfTest;
    private Date dateOfTest;
    private Long generateNumber;
    private String pathToScanDoc;
    private Long id;
    private Long moduleId;

    public CalibrationTestManualDTO() {}

    public CalibrationTestManualDTO(String serialNumber, Integer numberOfTest, Date dateOfTest, Long generateNumber, String pathToScanDoc, Long id) {
        this.serialNumber = serialNumber;
        this.numberOfTest = numberOfTest;
        this.dateOfTest = dateOfTest;
        this.generateNumber = generateNumber;
        this.pathToScanDoc = pathToScanDoc;
        this.id = id;
    }


}
