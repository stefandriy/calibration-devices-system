package com.softserve.edu.dto.provider;

import com.softserve.edu.entity.Organization;

import java.util.List;

public class VerificationUpdatingDTO {
    String idVerification;
    private List<String> idsOfVerifications;
    private Organization calibrator;

    private EmployeeProvider employeeProvider;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public Organization getCalibrator() {
        return calibrator;
    }

    public void setCalibrator(Organization calibrator) {
        this.calibrator = calibrator;
    }

    public EmployeeProvider getEmployeeProvider() {
        return employeeProvider;
    }

    public void setEmployeeProvider(EmployeeProvider employeeProvider) {
        this.employeeProvider = employeeProvider;
    }

    public String getIdVerification() {
        return idVerification;
    }

    public void setIdVerification(String idVerification) {
        this.idVerification = idVerification;
    }
}