package com.softserve.edu.dto.calibrator;


import lombok.Setter;
import lombok.Getter;

/**
 * @author Veronika
 */
@Getter
@Setter
public class ProtocolDTO {

    private String verificationId;

    private String sentToCalibratorDate;

    private String fullName;

    private String address;

    private String nameProvider;

    private String nameCalibrator;

    private String status;

    public ProtocolDTO() {

    }

    public ProtocolDTO(String verificationId, String sentToCalibratorDate, String firstName, String lastName, String middleName, String address,
                       String nameProvider, String nameCalibrator, String status ) {
        this.verificationId = verificationId;
        this.sentToCalibratorDate = sentToCalibratorDate;
        this.fullName = "" + firstName + " " + lastName + " " + middleName;
        this.address = address;
        this.nameProvider = nameProvider;
        this.nameCalibrator = nameCalibrator;
        this.status = status;
    }

}
