package com.softserve.edu.dto;


import java.util.List;
import java.util.Date;

/**
 * Created by Misha on 12/13/2015.
 */

public class CalibrationTestManualDTO {

    private String  serialNumber;
    private List<CalibrationTestDataManualDTO>listOfCalibrationTestDataManual;
    private Integer numberOfTest;
    private Date dateOfTest;
    private Long generatenumber;

    public CalibrationTestManualDTO() {}

    public CalibrationTestManualDTO(String serialNumber, Integer numberOfTest, Date dateOfTest, Long generateNumber) {
        this.serialNumber = serialNumber;
        this.numberOfTest = numberOfTest;
        this.dateOfTest = dateOfTest;
        this.generatenumber = generateNumber;
    }

    public Long getGeneratenumber() {
        return generatenumber;
    }

    public void setGeneratenumber(Long generatenumber) {
        this.generatenumber = generatenumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<CalibrationTestDataManualDTO> getListOfCalibrationTestDataManual() {
        return listOfCalibrationTestDataManual;
    }

    public void setListOfCalibrationTestDataManual(List<CalibrationTestDataManualDTO> listOfCalibrationTestDataManual) {
        this.listOfCalibrationTestDataManual = listOfCalibrationTestDataManual;
    }

    public Integer getNumberOfTest() {
        return numberOfTest;
    }

    public void setNumberOfTest(Integer numberOfTest) {
        this.numberOfTest = numberOfTest;
    }

    public Date getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(Date dateOfTest) {
        this.dateOfTest = dateOfTest;
    }
}
