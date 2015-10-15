package com.softserve.edu.service.state.verificator;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.utils.EmployeeDTO;

import java.util.List;

public interface StateVerificatorService {
    void saveStateVerificator(Organization stateVerificator);

    Organization findById(Long id);

    List<EmployeeDTO> getAllVerificatorEmployee(List<String> role, User employee);

    void assignVerificatorEmployee(String idVerification, User employeeCalibrator);
}

