package com.softserve.edu.dto.calibrator;

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

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, String providerName, String fullName,
                                       String address, String telephone){
        this.sentToCalibrator = sentDate;
        this.verficationId = verificationID;
        this.providerName = providerName;
        this.clientFullName = fullName;
        this.address = address;
        this.telephone = telephone;
    }

    public Date getSentToCalibrator() {
        return sentToCalibrator;
    }

    public void setSentToCalibrator(Date sentToCalibrator) {
        this.sentToCalibrator = sentToCalibrator;
    }

    public String getVerficationId() {
        return verficationId;
    }

    public void setVerficationId(String verficationId) {
        this.verficationId = verficationId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
