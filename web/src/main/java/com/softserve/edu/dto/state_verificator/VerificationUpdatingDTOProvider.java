package com.softserve.edu.dto.state_verificator;

import java.util.List;

/**
 * Created by Konyk on 10.08.2015.
 */
public class VerificationUpdatingDTOProvider {
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
