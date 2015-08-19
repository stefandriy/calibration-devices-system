package com.softserve.edu.dto.provider;

import java.util.List;

import com.softserve.edu.service.utils.EmployeeDTO;

/**
 * Created by MAX on 22.07.2015.
 */
public class VerificationProviderEmployeeDTO {
    String idVerification;
    private List<String> idsOfVerifications;

    private EmployeeDTO employeeDTO;

    private EmployeeDTO employeeCalibrator;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public String getIdVerification() {
        return idVerification;
    }

    public void setIdVerification(String idVerification) {
        this.idVerification = idVerification;
    }

    public EmployeeDTO getEmployeeCalibrator() {
        return employeeCalibrator;
    }

    public void setEmployeeCalibrator(EmployeeDTO employeeCalibrator) {
        this.employeeCalibrator = employeeCalibrator;
    }
}

