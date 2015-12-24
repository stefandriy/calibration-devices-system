package com.softserve.edu.dto.calibrator;


import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class NotStandardVerificationDTO {

    private String id;
    private Date initialDate;
    private String fullName;
    private String district;
    private String region;
    private String locality;
    private String street;
    private String building;
    private String flat;
    private Verification.ReadStatus readStatus;
    private Long calibrationTestId;
    private String symbol;
    private String standardSize;
    private String realiseYear;
    private Boolean dismantled;
    private String numberCounter;
    private Long counterId;
    private String fileName;
    private String stamp;
    private String testResult;

    public NotStandardVerificationDTO(String id, Date initialDate, Address address,
                                       String firstName, String lastName, String middleName,
                                       Counter counter, CalibrationTest tests) {
        this.id = id;
        this.initialDate = initialDate;
        this.fullName = firstName + " " + lastName + " " + middleName;
        this.street = address.getStreet();
        this.district = address.getDistrict();
        this.locality = address.getLocality();
        this.building = address.getBuilding();
        this.flat = address.getFlat();
        this.symbol = counter.getCounterType().getSymbol();
        this.standardSize = counter.getCounterType().getStandardSize();
        this.realiseYear = counter.getReleaseYear();
        this.stamp = counter.getStamp();
        this.fileName = tests.getName();
        this.testResult = tests.getTestResult().toString();
    }
    public NotStandardVerificationDTO(String id, Date initialDate, Address address,
                                      String firstName, String lastName, String middleName) {
        this.id = id;
        this.initialDate = initialDate;
        this.fullName = firstName + " " + lastName + " " + middleName;
        this.street = address.getStreet();
        this.district = address.getDistrict();
        this.locality = address.getLocality();

    }
}
