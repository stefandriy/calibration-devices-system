package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.Calibrator;

import java.util.List;

public class VerificationUpdatingDTO {
    private List<String> idsOfVerifications;
    private Calibrator calibrator;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public Calibrator getCalibrator() {
        return calibrator;
    }

    public void setCalibrator(Calibrator calibrator) {
        this.calibrator = calibrator;
    }
}
