package com.softserve.edu.dto;

/**
 * Created by Misha on 12/14/2015.
 */
public class CalibrationTestDataManualDTO {

    private String numberCounter;
    private String statusTestFirst;
    private String statusTestSecond;
    private String statusTestThird;
    private String statusCommon;
    private String id;
    private Long counterId;
    private CalibrationTestManualDTO calibrationTestManualDTO;

    public CalibrationTestDataManualDTO() {
    }

    public CalibrationTestDataManualDTO(String statusTestFirst, String statusTestSecond, String statusTestThird, String statusCommon, CalibrationTestManualDTO calibrationTestManualDTO) {
        this.statusTestFirst = statusTestFirst;
        this.statusTestSecond = statusTestSecond;
        this.statusTestThird = statusTestThird;
        this.statusCommon = statusCommon;
        this.calibrationTestManualDTO = calibrationTestManualDTO;
    }

    public CalibrationTestDataManualDTO(String numberCounter, String statusTestFirst, String statusTestSecond, String statusTestThird, String statusCommon,Long counterId, CalibrationTestManualDTO calibrationTestManualDTO) {
        this.numberCounter = numberCounter;
        this.statusTestFirst = statusTestFirst;
        this.statusTestSecond = statusTestSecond;
        this.statusTestThird = statusTestThird;
        this.statusCommon = statusCommon;
        this.counterId = counterId;
        this.calibrationTestManualDTO = calibrationTestManualDTO;
    }

    public CalibrationTestManualDTO getCalibrationTestManualDTO() {
        return calibrationTestManualDTO;
    }

    public void setCalibrationTestManualDTO(CalibrationTestManualDTO calibrationTestManualDTO) {
        this.calibrationTestManualDTO = calibrationTestManualDTO;
    }

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getNumberCounter() {
        return numberCounter;
    }

    public void setNumberCounter(String numberCounter) {
        this.numberCounter = numberCounter;
    }

    public String getStatusTestFirst() {
        return statusTestFirst;
    }

    public void setStatusTestFirst(String statusTestFirst) {
        this.statusTestFirst = statusTestFirst;
    }

    public String getStatusTestSecond() {
        return statusTestSecond;
    }

    public void setStatusTestSecond(String statusTestSecond) {
        this.statusTestSecond = statusTestSecond;
    }

    public String getStatusTestThird() {
        return statusTestThird;
    }

    public void setStatusTestThird(String statusTestThird) {
        this.statusTestThird = statusTestThird;
    }

    public String getStatusCommon() {
        return statusCommon;
    }

    public void setStatusCommon(String statusCommon) {
        this.statusCommon = statusCommon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
