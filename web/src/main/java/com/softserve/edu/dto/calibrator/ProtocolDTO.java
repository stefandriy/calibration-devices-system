package com.softserve.edu.dto.calibrator;

import lombok.Setter;
import lombok.Getter;

import java.util.Date;

/**
 * @author Veronika
 */
@Getter
@Setter
public class ProtocolDTO {

    private Date sentToCalibratorDate;

    private String fullName;

    private String adress;

    private String name;

    private String status;

    public ProtocolDTO() {

    }

    public ProtocolDTO(Date sentToCalibratorDate, String firstName, String lastName, String adress, String name, String status ) {
        this. sentToCalibratorDate = sentToCalibratorDate;
        this.fullName = "" + firstName + " " + lastName;
        this.adress = adress;
        this.name = name;
        this.status = status;
    }

}
