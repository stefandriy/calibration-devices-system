package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.Organization;

import java.util.List;

public class VerificationUpdatingDTO {

    private List<String> idsOfVerifications;
    private Long idsOfCalibrators;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }


    public Long getIdsOfCalibrators() {
        return idsOfCalibrators;
    }

    public void setIdsOfCalibrators(Long idsOfCalibrators) {
        this.idsOfCalibrators = idsOfCalibrators;
    }
}