package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.organization.Organization;

import java.util.Date;

/**
 * Created by Vasyl on 30.09.2015.
 */
public class VerificationPlanningTaskDTO {

    private Date sentToCalibrator;

    private String verficationId;

    private String providerName;

    private String clientFullName;

    private String address;

    private String telephone;

    public VerificationPlanningTaskDTO(){}

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, Organization organization, String fullName,
                                       String address, String telephone){
        this.sentToCalibrator = sentDate;
        this.verficationId = verificationID;
        this.providerName = organization.getName();
        this.clientFullName = fullName;
        this.address = address;
        this.telephone = telephone;
    }

}
