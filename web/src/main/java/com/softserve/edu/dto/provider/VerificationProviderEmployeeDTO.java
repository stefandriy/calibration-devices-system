package com.softserve.edu.dto.provider;

import com.softserve.edu.service.utils.*;
import com.softserve.edu.service.utils.EmployeeProvider;

import java.util.List;

/**
 * Created by MAX on 22.07.2015.
 */
public class VerificationProviderEmployeeDTO {
    String idVerification;
    private List<String> idsOfVerifications;

    private EmployeeProvider employeeProvider;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public com.softserve.edu.service.utils.EmployeeProvider getEmployeeProvider() {
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

