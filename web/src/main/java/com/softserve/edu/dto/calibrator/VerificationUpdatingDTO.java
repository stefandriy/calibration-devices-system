package com.softserve.edu.dto.calibrator;

import java.util.List;

import com.softserve.edu.entity.Organization;

public class VerificationUpdatingDTO {
    private List<String> idsOfVerifications;
    private Organization verificator;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public Organization getVerificator() {
        return verificator;
    }

    public void setVerificator(Organization verificator) {
        this.verificator = verificator;
    }
}
