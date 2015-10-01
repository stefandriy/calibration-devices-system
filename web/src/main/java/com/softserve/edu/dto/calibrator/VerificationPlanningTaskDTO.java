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

    private String adress;

    private String telephone;

    public VerificationPlanningTaskDTO(){}

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, Organization organization, String firstName,
                                       String secondName, String lastName, String locality, String street, String bulding, String flat,
                                       String telephone ){
        this.sentToCalibrator = sentDate;
        this.verficationId = verificationID;
        this.providerName = organization.getName();
        this.clientFullName = firstName + secondName + lastName;
        this.adress = locality + street + bulding + flat;
        this.telephone = telephone;
    }

}
