package com.softserve.edu.dto.provider;

import java.util.List;

import com.softserve.edu.service.utils.EmployeeProvider;

/**
 * Created by MAX on 22.07.2015.
 */
public class VerificationProviderEmployeeDTO {
    String idVerification;
    private List<String> idsOfVerifications;

    private EmployeeProvider employeeProvider;

    private EmployeeProvider employeeCalibrator;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
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

    public EmployeeProvider getEmployeeCalibrator() {
        return employeeCalibrator;
    }

    public void setEmployeeCalibrator(EmployeeProvider employeeCalibrator) {
        this.employeeCalibrator = employeeCalibrator;
    }
}

