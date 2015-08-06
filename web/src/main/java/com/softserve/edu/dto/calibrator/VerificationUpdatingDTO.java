package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.StateVerificator;

import java.util.List;

public class VerificationUpdatingDTO {
    private List<String> idsOfVerifications;
    private StateVerificator verificator;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public StateVerificator getVerificator() {
        return verificator;
    }

    public void setVerificator(StateVerificator verificator) {
        this.verificator = verificator;
    }
}
