package com.softserve.edu.service.utils;


import org.apache.commons.lang3.builder.ToStringBuilder;

public class BBIOutcomeDTO {
    private String verificationID;
    private String bbiFileName;
    private boolean success;

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public String getBbiFileName() {
        return bbiFileName;
    }

    public void setBbiFileName(String bbiFileName) {
        this.bbiFileName = bbiFileName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BBIOutcomeDTO(String bbiFileName, String verificationID, boolean success) {

        this.verificationID = verificationID;
        this.bbiFileName = bbiFileName;
        this.success = success;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("verificationID", verificationID)
                .append("bbiFileName", bbiFileName)
                .append("success", success)
                .toString();
    }
}
