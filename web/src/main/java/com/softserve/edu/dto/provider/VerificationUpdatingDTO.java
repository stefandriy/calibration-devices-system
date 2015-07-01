package com.softserve.edu.dto.provider;

import com.softserve.edu.dto.admin.EmployeeProvider;
import com.softserve.edu.entity.Calibrator;
import com.softserve.edu.entity.user.ProviderEmployee;

import java.util.List;

public class VerificationUpdatingDTO {

    private List<String> idsOfVerifications;
    private Calibrator calibrator;

    private EmployeeProvider employeeProvider;

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

    public EmployeeProvider getEmployeeProvider() {
        return employeeProvider;
    }

    public void setEmployeeProvider(EmployeeProvider employeeProvider) {
        this.employeeProvider = employeeProvider;
    }
}